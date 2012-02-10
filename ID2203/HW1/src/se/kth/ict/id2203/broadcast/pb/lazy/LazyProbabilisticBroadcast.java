/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import java.util.*;
import se.kth.ict.id2203.broadcast.pb.PbBroadcast;
import se.kth.ict.id2203.broadcast.pb.PbDeliver;
import se.kth.ict.id2203.broadcast.pb.ProbabilisticBroadcast;
import se.kth.ict.id2203.broadcast.un.UnBroadcast;
import se.kth.ict.id2203.broadcast.un.UnDeliver;
import se.kth.ict.id2203.broadcast.un.UnreliableBroadcast;
import se.kth.ict.id2203.broadcast.un.simple.DataMessage;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.flp2p.Flp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author Igor
 */
public class LazyProbabilisticBroadcast extends ComponentDefinition {
    //ports
    Negative<ProbabilisticBroadcast> pb = provides(ProbabilisticBroadcast.class);
    Positive<UnreliableBroadcast> un = requires(UnreliableBroadcast.class);
    Positive<FairLossPointToPointLink> flp2p = requires(FairLossPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    //consts
    private final static String SN_DELIMITER = ";";
    //local variables    
    private Address self;
    private Set<Address> neighborSet;
    private Map<Address, Integer> next;
    private int lsn;
    private Set<DataMessage> pending;
    private Set<DataMessage> stored;
    private Random rand;
    private long seed;
    private int fanout;
    private float alpha;
    private int delta;
    private int maxRounds;

    public LazyProbabilisticBroadcast() {
        subscribe(initHandler, control);
        subscribe(pbBroadcastHandler, pb);
        subscribe(unDeliverHandler, un);
        subscribe(requestMessageHandler, flp2p);
        subscribe(dataMessageHandler, flp2p);
        subscribe(timeoutHandler, timer);
    }
    //handlers
    Handler<LazyProbabilisticBroadcastInit> initHandler = new Handler<LazyProbabilisticBroadcastInit>() {
        @Override
        public void handle(LazyProbabilisticBroadcastInit event) {
            self = event.getSelf();
            neighborSet = event.getNeighborSet();
            seed = event.getSeed();
            rand = new Random(seed);
            fanout = event.getFanout();
            alpha = event.getAlpha();
            delta = event.getDelta();
            maxRounds = event.getMaxRounds();

            next = new HashMap<Address, Integer>();
            for (Address p : neighborSet) {
                next.put(p, 1);
            }

            lsn = 0;

            pending = new HashSet<DataMessage>();
            stored = new HashSet<DataMessage>();
        }
    };
    Handler<PbBroadcast> pbBroadcastHandler = new Handler<PbBroadcast>() {
        @Override
        public void handle(PbBroadcast event) {
            ++lsn;
            trigger(new UnBroadcast(lsn + SN_DELIMITER + event.getMessage()), un); //piggybacking sn as a prefix to message
        }
    };
    Handler<UnDeliver> unDeliverHandler = new Handler<UnDeliver>() {
        @Override
        public void handle(UnDeliver event) {
            Address s = event.getSource();
            String m = event.getMessage();

            DataMessage current = new DataMessage(s, m);
            if (rand.nextFloat() > alpha) {
                stored.add(current);
            }

            int sn = extractSequenceNumber(m);
            String message = extractMessage(m);
            if (sn == next.get(s)) {
                next.put(s, next.remove(s) + 1);
                trigger(new PbDeliver(s, message), pb);
            } else if (sn > next.get(s)) {
                pending.add(current);
                for (int missing = next.get(s); missing < sn - 1; missing++) {
                    boolean hasMissing = false;
                    for (DataMessage dm : pending) {
                        if ((s == dm.getSource())
                                && (missing == extractSequenceNumber(dm.getMessage()))) {
                            hasMissing = true;
                            break;
                        }
                    }
                    if (!hasMissing) {
                        gossip(new RequestMessage(self, s, missing, maxRounds - 1));
                    }
                }

                ScheduleTimeout st = new ScheduleTimeout(delta);
                st.setTimeoutEvent(new PullingTimeout(st, s, sn));
                trigger(st, timer);
            }
        }
    };
    Handler<RequestMessage> requestMessageHandler = new Handler<RequestMessage>() {
        @Override
        public void handle(RequestMessage event) {
            Address q = event.getSource();
            Address s = event.getBroadcastSource();
            int sn = event.getSequenceNumber();

            DataMessage missingMessage = null;
            for (DataMessage dm : stored) {
                if ((s == dm.getSource())
                        && (sn == extractSequenceNumber(dm.getMessage()))) {
                    missingMessage = dm;
                    break;
                }
            }
            if (missingMessage != null) {
                trigger(new Flp2pSend(q, missingMessage), flp2p);
            } else if (event.ttl > 0) {
                --event.ttl;
                gossip(event);
            }
        }
    };
    Handler<DataMessage> dataMessageHandler = new Handler<DataMessage>() {
        @Override
        public void handle(DataMessage event) {
            pending.add(event);

            //upon exists...
            boolean hasNext = false;
            do {
                Iterator<DataMessage> i = pending.iterator();
                while (i.hasNext()) {
                    DataMessage dm = i.next();
                    int sn = extractSequenceNumber(dm.getMessage());
                    String message = extractMessage(dm.getMessage());
                    Address s = dm.getSource();
                    if (sn == next.get(s)) {
                        next.put(s, next.remove(s) + 1);
                        i.remove();
                        trigger(new PbDeliver(s, message), pb);
                        hasNext = true;
                    }
                }
            } while (hasNext);
        }
    };
    Handler<PullingTimeout> timeoutHandler = new Handler<PullingTimeout>() {
        @Override
        public void handle(PullingTimeout event) {
            Address s = event.getBroadcastSource();
            int sn = event.getSequenceNumber();

            if (sn > next.get(s)) {
                next.remove(s);
                next.put(s, sn + 1);
            }
        }
    };

    //procedures and functions
    private Set<Address> pickTargets(int k) {
        if (k > neighborSet.size()) {
            return new HashSet<Address>(neighborSet);
        }

        Set<Address> candidates = new HashSet<Address>(neighborSet);
        candidates.remove(self);
        Set<Address> targets = new HashSet<Address>();
        while (targets.size() < k) {
            Address candidate = (Address) candidates.toArray()[rand.nextInt(candidates.size())];
            candidates.remove(candidate);
            targets.add(candidate);
        }

        return targets;
    }

    private void gossip(RequestMessage request) {
        for (Address t : pickTargets(fanout)) {
            trigger(new Flp2pSend(t, request), flp2p);
        }
    }

    private int extractSequenceNumber(String message) {
        int pos = message.indexOf(SN_DELIMITER);
        return Integer.parseInt(message.substring(0, pos));
    }

    private String extractMessage(String message) {
        int pos = message.indexOf(SN_DELIMITER);
        return message.substring(pos + 1);
    }
}
