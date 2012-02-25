/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwcm;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.broadcast.beb.BebBroadcast;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.links.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.links.pp2p.Pp2pSend;
import se.kth.ict.id2203.registers.atomic.*;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class ReadImposeWriteConsultMajorityAtomicRegister extends ComponentDefinition {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(ReadImposeWriteConsultMajorityAtomicRegister.class);
    //ports
    Negative<AtomicRegister> nnar = provides(AtomicRegister.class);
    Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    //local variables
    private Address self;
    private Set<Address> all;
    private int i;
    private int registerNumber;
    private List<Set<Address>> writeSet;
    private List<Set<ReadSetEntry>> readSet;
    private boolean[] reading;
    private int[] reqid;
    private int[] v;
    private int[] ts;
    private int[] mrank;
    private int[] writeval;
    private int[] readval;
    private boolean readMajority;
    private boolean writeMajority;

    public ReadImposeWriteConsultMajorityAtomicRegister() {
        subscribe(initHandler, control);
        subscribe(readRequestHandler, nnar);
        subscribe(writeRequestHandler, nnar);
        subscribe(readMessageHandler, beb);
        subscribe(writeMessageHandler, beb);
        subscribe(readValMessageHandler, pp2p);
        subscribe(ackMessageHandler, pp2p);
    }
    //handlers
    Handler<ReadImposeWriteConsultMajorityAtomicRegisterInit> initHandler = new Handler<ReadImposeWriteConsultMajorityAtomicRegisterInit>() {
        @Override
        public void handle(ReadImposeWriteConsultMajorityAtomicRegisterInit event) {
            all = event.getAll();
            self = event.getSelf();
            registerNumber = event.getRegisterNumber();

            writeSet = new ArrayList<Set<Address>>(registerNumber);
            readSet = new ArrayList<Set<ReadSetEntry>>(registerNumber);
            reading = new boolean[registerNumber];
            reqid = new int[registerNumber];
            v = new int[registerNumber];
            ts = new int[registerNumber];
            mrank = new int[registerNumber];
            writeval = new int[registerNumber];
            readval = new int[registerNumber];

            i = rank();
            for (int j = 0; j < registerNumber; j++) {
                writeSet.add(new HashSet<Address>());
                readSet.add(new HashSet<ReadSetEntry>());
            }
        }
    };
    Handler<ReadRequest> readRequestHandler = new Handler<ReadRequest>() {
        @Override
        public void handle(ReadRequest event) {
            logger.debug("Invoking reading operation on register {}...", event.getRegister());
            int r = event.getRegister();

            ++reqid[r];
            reading[r] = true;
            readSet.get(r).clear();
            writeSet.get(r).clear();
            readMajority = false;
            writeMajority = false;
            trigger(new BebBroadcast(new ReadMessage(self, r, reqid[r])), beb);
        }
    };
    Handler<WriteRequest> writeRequestHandler = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest event) {
            logger.debug("Invoking writing operation on register {} with value={}...", event.getRegister(), event.getValue());
            int r = event.getRegister();
            int val = event.getValue();

            ++reqid[r];
            writeval[r] = val;
            readSet.get(r).clear();
            writeSet.get(r).clear();
            readMajority = false;
            writeMajority = false;
            trigger(new BebBroadcast(new ReadMessage(self, r, reqid[r])), beb);
        }
    };
    Handler<ReadMessage> readMessageHandler = new Handler<ReadMessage>() {
        @Override
        public void handle(ReadMessage event) {
            Address source = event.getSource();
            int r = event.getRegister();
            int id = event.getRequestId();
            logger.debug("Read message from {}: [r={} id={}]",
                    new Object[]{event.getSource(), event.getRegister(), event.getRequestId()});
            trigger(new Pp2pSend(source, new ReadValMessage(self, r, id, ts[r], mrank[r], v[r])), pp2p);
        }
    };
    Handler<ReadValMessage> readValMessageHandler = new Handler<ReadValMessage>() {
        @Override
        public void handle(ReadValMessage event) {
            logger.debug("ReadVal from {}: [r={} id={} t={} rk={} val={}]",
                    new Object[]{event.getSource(), event.getRegister(), event.getRequestId(), event.getTimestamp(), event.getRank(), event.getValue()});
            int r = event.getRegister();
            int id = event.getRequestId();
            int t = event.getTimestamp();
            int rk = event.getRank();
            int val = event.getValue();

            if (id == reqid[r] && !readMajority) {
                readSet.get(r).add(new ReadSetEntry(t, rk, val));
                checkReadSet();
            }
        }
    };
    Handler<WriteMessage> writeMessageHandler = new Handler<WriteMessage>() {
        @Override
        public void handle(WriteMessage event) {
            Address source = event.getSource();
            int r = event.getRegister();
            int id = event.getRequestId();
            int t = event.getTimestamp();
            int j = event.getRank();
            int val = event.getValue();
            logger.debug("Write message from {}: [r={} id={} t={} rk={} val={}]",
                    new Object[]{event.getSource(), event.getRegister(), event.getRequestId(), event.getTimestamp(), event.getRank(), event.getValue()});

            if ((t > ts[r])
                    || (t == ts[r] && j > mrank[r])) {
                v[r] = val;
                ts[r] = t;
                mrank[r] = j;
            }
            trigger(new Pp2pSend(source, new AckMessage(self, r, id)), pp2p);
        }
    };
    Handler<AckMessage> ackMessageHandler = new Handler<AckMessage>() {
        @Override
        public void handle(AckMessage event) {
            logger.debug("Ack from {}: [r={} id={}]",
                    new Object[]{event.getSource(), event.getRegister(), event.getRequestId()});
            Address source = event.getSource();
            int r = event.getRegister();
            int id = event.getRequestId();

            if (id == reqid[r] && !writeMajority) {
                writeSet.get(r).add(source);
                checkWriteSet();
            }
        }
    };

    //upon internal event
    private void checkReadSet() {
        for (int r = 0; r < registerNumber; r++) {
            if (readSet.get(r).size() > all.size() / 2) {
                logger.debug("Read majority");
                readMajority = true;
                int val = 0;
                int t = -1;
                int rk = -1;
                for (ReadSetEntry entry : readSet.get(r)) {
                    if ((entry.getTimestamp() > t)
                            || (entry.getTimestamp() == t && entry.getRank() > rk)) {
                        t = entry.getTimestamp();
                        rk = entry.getRank();
                        val = entry.getValue();
                    }
                }
                readval[r] = val;
                if (reading[r]) {
                    trigger(new BebBroadcast(new WriteMessage(self, r, reqid[r], t, rk, readval[r])), beb);
                } else {
                    trigger(new BebBroadcast(new WriteMessage(self, r, reqid[r], t + 1, i, writeval[r])), beb);
                }
            }
        }
    }

    private void checkWriteSet() {
        for (int r = 0; r < registerNumber; r++) {
            if (writeSet.get(r).size() > all.size() / 2) {
                logger.debug("Write majority");
                writeMajority = true;
                if (reading[r]) {
                    reading[r] = false;
                    trigger(new ReadResponse(r, readval[r]), nnar);
                } else {
                    trigger(new WriteResponse(r), nnar);
                }
            }
        }
    }

    //procedures and functions
    private int rank() {
//        int rank = 0;
//        for (Address addr : all) {
//            if (self.equals(addr)) {
//                logger.debug("Rank={}", rank);
//                return rank;
//            }
//            ++rank;
//        }
//        return -1;
        logger.debug("Rank={}", self.getId());
        return self.getId();
    }

    private class ReadSetEntry {
        private int timestamp;
        private int rank;
        private int value;

        public ReadSetEntry(int timestamp, int rank, int value) {
            this.timestamp = timestamp;
            this.rank = rank;
            this.value = value;
        }

        public int getRank() {
            return rank;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "{" + timestamp + ":" + rank + ":" + value + '}';
        }
    }
}
