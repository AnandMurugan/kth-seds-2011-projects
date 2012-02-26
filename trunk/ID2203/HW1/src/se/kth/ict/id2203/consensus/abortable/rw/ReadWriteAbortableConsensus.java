/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.abortable.rw;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import se.kth.ict.id2203.broadcast.beb.BebBroadcast;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.consensus.abortable.*;
import se.kth.ict.id2203.link.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.link.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class ReadWriteAbortableConsensus extends ComponentDefinition {
    //ports
    Negative<AbortableConsensus> ac = provides(AbortableConsensus.class);
    Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    //local variables
    private Address self;
    private int n;
    private Set<Integer> seenIds;
    private int majority;
    private Map<Integer, Object> tempValue;
    private Map<Integer, Object> val;
    private Map<Integer, Integer> wAcks;
    private Map<Integer, Integer> rts;
    private Map<Integer, Integer> wts;
    private Map<Integer, Integer> tstamp;
    private Map<Integer, Set<ReadSetEntry>> readSet;

    public ReadWriteAbortableConsensus() {
        subscribe(initHandler, control);
        subscribe(acProposeHandler, ac);
        subscribe(readMessageHandler, beb);
        subscribe(writeMessageHandler, beb);
        subscribe(nAckMessageHandler, pp2p);
        subscribe(readAckMessageHandler, pp2p);
        subscribe(writeAckMessageHandler, pp2p);
    }
    //handlers
    Handler<ReadWriteAbortableConsensusInit> initHandler = new Handler<ReadWriteAbortableConsensusInit>() {
        @Override
        public void handle(ReadWriteAbortableConsensusInit event) {
            self = event.getSelf();
            n = event.getN();

            seenIds = new TreeSet<Integer>();
            tempValue = new HashMap<Integer, Object>();
            val = new HashMap<Integer, Object>();
            wAcks = new HashMap<Integer, Integer>();
            rts = new HashMap<Integer, Integer>();
            wts = new HashMap<Integer, Integer>();
            tstamp = new HashMap<Integer, Integer>();
            readSet = new HashMap<Integer, Set<ReadSetEntry>>();
            majority = n / 2 + 1;
        }
    };
    Handler<AcPropose> acProposeHandler = new Handler<AcPropose>() {
        @Override
        public void handle(AcPropose event) {
            int id = event.getId();
            Object value = event.getValue();

            initInstance(id);
            tstamp.put(id, tstamp.get(id) + n);
            tempValue.put(id, value);
            trigger(new BebBroadcast(new ReadMessage(self, id, tstamp.get(id))), beb);
        }
    };
    Handler<ReadMessage> readMessageHandler = new Handler<ReadMessage>() {
        @Override
        public void handle(ReadMessage event) {
            Address source = event.getSource();
            int id = event.getId();
            int ts = event.getTimestamp();

            if (rts.get(id) >= ts || wts.get(id) >= ts) {
                trigger(new Pp2pSend(source, new NAckMessage(self, id)), pp2p);
            } else {
                rts.put(id, ts);
                trigger(new Pp2pSend(source, new ReadAckMessage(self, id, wts.get(id), val.get(id), ts)), pp2p);
            }
        }
    };
    Handler<NAckMessage> nAckMessageHandler = new Handler<NAckMessage>() {
        @Override
        public void handle(NAckMessage event) {
            int id = event.getId();

            readSet.get(id).clear();
            wAcks.put(id, 0);
            trigger(new AcDecide(id, null), ac);
        }
    };
    Handler<ReadAckMessage> readAckMessageHandler = new Handler<ReadAckMessage>() {
        @Override
        public void handle(ReadAckMessage event) {
            int id = event.getId();
            int ts = event.getTimestamp();
            Object v = event.getValue();
            int sentts = event.getSentTimestamp();

            if (sentts == tstamp.get(id)) {
                readSet.get(id).add(new ReadSetEntry(ts, v));
                if (readSet.get(id).size() == majority) {
                    ReadSetEntry ts_v = highest(readSet.get(id));
                    if (ts_v.getValue() != null) {
                        tempValue.put(id, ts_v.getValue());
                    }
                    trigger(new BebBroadcast(new WriteMessage(self, id, tstamp.get(id), tempValue.get(id))), beb);
                }
            }
        }
    };
    Handler<WriteMessage> writeMessageHandler = new Handler<WriteMessage>() {
        @Override
        public void handle(WriteMessage event) {
            Address source = event.getSource();
            int id = event.getId();
            int ts = event.getTimestamp();
            Object v = event.getValue();

            initInstance(id);
            if (rts.get(id) > ts || wts.get(id) > ts) {
                trigger(new Pp2pSend(source, new NAckMessage(self, id)), pp2p);
            } else {
                val.put(id, v);
                wts.put(id, ts);
                trigger(new Pp2pSend(source, new WriteAckMessage(self, id, ts)), pp2p);
            }
        }
    };
    Handler<WriteAckMessage> writeAckMessageHandler = new Handler<WriteAckMessage>() {
        @Override
        public void handle(WriteAckMessage event) {
            int id = event.getId();
            int sentts = event.getSentTimestamp();

            if (sentts == tstamp.get(id)) {
                wAcks.put(id, wAcks.get(id) + 1);
                if (wAcks.get(id) == majority) {
                    readSet.get(id).clear();
                    wAcks.put(id, 0);
                    trigger(new AcDecide(id, tempValue.get(id)), ac);
                }
            }
        }
    };

    //procedures and functions
    private void initInstance(int id) {
        if (!seenIds.contains(id)) {
            tempValue.put(id, null);
            val.put(id, null);
            wAcks.put(id, 0);
            rts.put(id, 0);
            wts.put(id, 0);
            tstamp.put(id, rank(self));
            readSet.put(id, new HashSet<ReadSetEntry>());
            seenIds.add(id);
        }
    }

    private int rank(Address node) {
        return node.getId();
    }

    private ReadSetEntry highest(Set<ReadSetEntry> set) {
        int ts = Integer.MIN_VALUE;
        ReadSetEntry highest = null;
        for (ReadSetEntry entry : set) {
            if (entry.getTimestamp() > ts) {
                ts = entry.getTimestamp();
                highest = entry;
            }
        }
        return highest;
    }

    private class ReadSetEntry {
        private int timestamp;
        private Object value;

        public ReadSetEntry(int timestamp, Object value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public Object getValue() {
            return value;
        }
    }
}
