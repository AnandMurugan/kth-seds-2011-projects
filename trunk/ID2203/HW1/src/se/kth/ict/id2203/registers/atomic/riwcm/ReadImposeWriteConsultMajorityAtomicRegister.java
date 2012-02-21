/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwcm;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.broadcast.beb.BebBroadcast;
import se.kth.ict.id2203.broadcast.beb.BebDeliver;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
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
    //consts
    public final static String DELIM = " ";
    public final static int READ = 1;
    public final static int WRITE = 2;
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
        subscribe(bebDeliverHandler, beb);
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
            trigger(new BebBroadcast(READ + DELIM + r + DELIM + reqid[r]), beb);
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
            trigger(new BebBroadcast(READ + DELIM + r + DELIM + reqid[r]), beb);
        }
    };
    Handler<BebDeliver> bebDeliverHandler = new Handler<BebDeliver>() {
        @Override
        public void handle(BebDeliver event) {
            Address source = event.getSource();
            String[] data = tokenize(event.getMessage());

            int type = Integer.parseInt(data[0]);
            int r = Integer.parseInt(data[1]);
            int id = Integer.parseInt(data[2]);
            switch (type) {
                case READ:
                    logger.debug("Read message from {}: [{}]", event.getSource(), event.getMessage());
                    trigger(new Pp2pSend(source, new ReadValMessage(self, r, id, ts[r], mrank[r], v[r])), pp2p);
                    break;
                case WRITE:
                    logger.debug("Write message from {}: [{}]", event.getSource(), event.getMessage());
                    int t = Integer.parseInt(data[3]);
                    int j = Integer.parseInt(data[4]);
                    int val = Integer.parseInt(data[5]);

                    if ((t > ts[r])
                            || (t == ts[r] && j > mrank[r])) {
                        v[r] = val;
                        ts[r] = t;
                        mrank[r] = j;
                    }
                    trigger(new Pp2pSend(source, new AckMessage(self, r, id)), pp2p);
                    break;

            }
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
                checkReadSetEvent();
            }
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
                checkWriteSetEvent();
            }
        }
    };

    //upon internal event
    private void checkReadSetEvent() {
        logger.debug("N/2 = {}; ReadSets = {}", (all.size() / 2), readSet);
        for (int r = 0; r < registerNumber; r++) {
            if (readSet.get(r).size() > all.size() / 2) {
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
                    trigger(new BebBroadcast(WRITE + DELIM + r + DELIM + reqid[r] + DELIM + t + DELIM + rk + DELIM + readval[r]), beb);
                } else {
                    trigger(new BebBroadcast(WRITE + DELIM + r + DELIM + reqid[r] + DELIM + (t + 1) + DELIM + i + DELIM + writeval[r]), beb);
                }
            }
        }
    }

    private void checkWriteSetEvent() {
        logger.debug("N/2 = {}; WriteSets = {}", (all.size() / 2), writeSet);
        for (int r = 0; r < registerNumber; r++) {
            if (writeSet.get(r).size() > all.size() / 2) {
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
        int rank = 0;
        for (Address addr : all) {
            if (self.equals(addr)) {
                logger.debug("Rank={}", rank);
                return rank;
            }
            ++rank;
        }
        return -1;
    }

    private String[] tokenize(String m) {
        StringTokenizer st = new StringTokenizer(m, DELIM);
        String[] res = new String[st.countTokens()];
        int j = 0;
        while (st.hasMoreTokens()) {
            res[j++] = st.nextToken();
        }
        return res;
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

//        @Override
//        public boolean equals(Object obj) {
//            if (obj == null) {
//                return false;
//            }
//            if (getClass() != obj.getClass()) {
//                return false;
//            }
//            final ReadSetEntry other = (ReadSetEntry) obj;
//            if (this.timestamp != other.timestamp) {
//                return false;
//            }
//            if (this.rank != other.rank) {
//                return false;
//            }
//            if (this.value != other.value) {
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        public int hashCode() {
//            int hash = 3;
//            hash = 11 * hash + this.timestamp;
//            hash = 11 * hash + this.rank;
//            hash = 11 * hash + this.value;
//            return hash;
//        }
        @Override
        public String toString() {
            return "{" + timestamp + ":" + rank + ":" + value + '}';
        }
    }
}
