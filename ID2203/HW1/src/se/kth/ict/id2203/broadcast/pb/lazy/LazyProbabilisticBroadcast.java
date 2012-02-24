/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.broadcast.pb.PbBroadcast;
import se.kth.ict.id2203.broadcast.pb.PbDeliver;
import se.kth.ict.id2203.broadcast.pb.ProbabilisticBroadcast;
import se.kth.ict.id2203.broadcast.un.UnBroadcast;
import se.kth.ict.id2203.broadcast.un.UnreliableBroadcast;
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
    //logger
    private static final Logger logger = LoggerFactory.getLogger(LazyProbabilisticBroadcast.class);
    //local variables    
    private Address self;
    private Set<Address> neighborSet;
    private Map<Address, Integer> next;
    private int lsn;
    private Set<DataMessage> pending = new TreeSet<DataMessage>();
    private Set<DataMessage> stored = new HashSet<DataMessage>();
    private Random rand;
    private long seed;
    private int fanout;
    private float alpha;
    private int delta;
    private int maxRounds;

    public LazyProbabilisticBroadcast() {
        subscribe(initHandler, control);
        subscribe(pbBroadcastHandler, pb);
        subscribe(lazyProbabilisticBroadcastMessageHandler, un);
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
            next.put(self, 1);

            lsn = 0;

        }
    };
    Handler<PbBroadcast> pbBroadcastHandler = new Handler<PbBroadcast>() {
        @Override
        public void handle(PbBroadcast event) {
            ++lsn;
            trigger(new UnBroadcast(new LazyProbabilisticBroadcastMessage(self, event.getDeliverEvent(), lsn)), un);
        }
    };
    Handler<LazyProbabilisticBroadcastMessage> lazyProbabilisticBroadcastMessageHandler = new Handler<LazyProbabilisticBroadcastMessage>() {
        @Override
        public void handle(LazyProbabilisticBroadcastMessage event) {
            Address s = event.getSource();
            int sn = event.getSequenceNumber();
            PbDeliver message = event.getDeliverEvent();

            DataMessage current = new DataMessage(s, message, sn);
            if (rand.nextFloat() < alpha) {
                logger.debug("Storing messages #{} from {}", sn, s);
                stored.add(current);
            }

            if (sn == next.get(s)) {
                next.put(s, next.remove(s) + 1);
                trigger(message, pb);
            } else if (sn > next.get(s)) {
                logger.debug("Missing some messages: from {} to {}", next.get(s), sn - 1);
                pending.add(current);
                for (int missing = next.get(s); missing < sn; missing++) {
                    logger.debug("Looking for message #{} from {}", missing, s);
                    boolean hasMissing = false;
                    for (DataMessage dm : pending) {
                        if ((s.equals(dm.getSource()))
                                && (missing == dm.getSequenceNumber())) {
                            hasMissing = true;
                            logger.debug("Found message #{} from {} in pending", missing, s);
                            break;
                        }
                    }
                    if (!hasMissing) {
                        logger.debug("Gossiping to retrieve message #{} from {}", missing, s);
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
                if ((s.equals(dm.getSource()))
                        && (sn == dm.getSequenceNumber())) {
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
            if (event.getSequenceNumber() < next.get(event.getSource())) {
                return;
            }
            pending.add(event);
            checkPending();
        }
    };
    Handler<PullingTimeout> timeoutHandler = new Handler<PullingTimeout>() {
        @Override
        public void handle(PullingTimeout event) {
            Address s = event.getBroadcastSource();
            int sn = event.getSequenceNumber();

            if (sn > next.get(s)) {
                logger.debug("Skipping missing messages #{}-#{} from {} due to timeout...", new Object[]{next.get(s), sn - 1, s});
                next.remove(s);
                next.put(s, sn + 1);

                logger.debug("Delivering pending messages...");
                logger.debug("Pending before={}", pending);
                deliverPending(s, sn);
                logger.debug("Pending after={}", pending);
            }
        }
    };

    //procedures and functions
    private Set<Address> pickTargets(int k) {
        //logger.debug("Picking targets...");
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

        //logger.debug("Targets: {}", targets);
        return targets;
    }

    private void gossip(RequestMessage request) {
        for (Address t : pickTargets(fanout)) {
            trigger(new Flp2pSend(t, request), flp2p);
        }
    }

    private void checkPending() {
        //upon exists...
        boolean hasNext;
        do {
            hasNext = false;
            Iterator<DataMessage> i = pending.iterator();
            while (i.hasNext()) {
                DataMessage dm = i.next();
                int sn = dm.getSequenceNumber();
                PbDeliver message = dm.getDeliverEvent();
                Address s = dm.getSource();
                if (sn == next.get(s)) {
                    next.put(s, next.remove(s) + 1);
                    i.remove();
                    trigger(message, pb);
                    hasNext = true;
                }
            }
        } while (hasNext);
    }

    private void deliverPending(Address s, int sn) {
        Iterator<DataMessage> i = pending.iterator();
        while (i.hasNext()) {
            DataMessage dm = i.next();
            PbDeliver message = dm.getDeliverEvent();
            int sequenceNumber = dm.getSequenceNumber();
            Address source = dm.getSource();

            if (source.equals(s) && (sequenceNumber <= sn)) {
                i.remove();
                trigger(message, pb);
            }
        }
    }
}
