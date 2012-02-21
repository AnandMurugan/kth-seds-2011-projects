/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwcm;

import java.util.*;
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
    //ports
    Negative<AtomicRegister> nnar = provides(AtomicRegister.class);
    Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    //consts
    public final static String DELIM = "\t";
    public final static int READ = 1;
    public final static int WRITE = 2;
    //local variables
    private Address self;
    private Set<Address> neighbors;
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
            neighbors = event.getNeighbors();
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

            i = rank(self, all);
            for (int j = 0; j < registerNumber; j++) {
                writeSet.add(new HashSet<Address>());
                readSet.add(new HashSet<ReadSetEntry>());
            }
        }
    };
    Handler<ReadRequest> readRequestHandler = new Handler<ReadRequest>() {
        @Override
        public void handle(ReadRequest event) {
            int r = event.getR();

            ++reqid[r];
            reading[r] = true;
            readSet.get(r).clear();
            writeSet.get(r).clear();
            trigger(new BebBroadcast(READ + DELIM + r + DELIM + reqid[r]), beb);
        }
    };
    Handler<WriteRequest> writeRequestHandler = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest event) {
            int r = event.getR();
            int val = event.getVal();

            ++reqid[r];
            writeval[r] = val;
            readSet.get(r).clear();
            writeSet.get(r).clear();
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
                    trigger(new Pp2pSend(source, new ReadValMessage(self, r, id, ts[r], mrank[r], v[r])), pp2p);
                    break;
                case WRITE:
                    int t = Integer.parseInt(data[3]);
                    int j = Integer.parseInt(data[4]);
                    int val = Integer.parseInt(data[5]);

                    if (true/*
                             * (t,j)>(ts[r],mrank[r])
                             */) {
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
            Address source = event.getSource();
            int r = event.getRegister();
            int id = event.getRequestId();
            int t = event.getTimestamp();
            int rk = event.getRank();
            int val = event.getValue();

            if (id == reqid[r]) {
                readSet.get(r).add(new ReadSetEntry(t, rk, val));
                checkReadSetEvent();
            }
        }
    };
    Handler<AckMessage> ackMessageHandler = new Handler<AckMessage>() {
        @Override
        public void handle(AckMessage event) {
            Address source = event.getSource();
            int r = event.getRegister();
            int id = event.getRequestId();

            if (id == reqid[r]) {
                writeSet.get(r).add(source);
                checkWriteSetEvent();
            }
        }
    };

    //upon internal event
    private void checkReadSetEvent() {
        for (int r = 0; r < registerNumber; r++) {
            if (readSet.get(r).size() > all.size() / 2) {
                int val = 0;
                int t = 0;
                int rk = 0;
                for (ReadSetEntry entry : readSet.get(r)) {
                    if (true/*
                             * (entry.getTimestamp,entry.getRank)>(highestTimestamp,
                             * highestRank)
                             */) {
                        t = entry.getTimestamp();
                        rk = entry.getRank();
                        val = entry.getValue();
                    }
                }
                readval[r] = val;
                if (reading[r]) {
                    trigger(new BebBroadcast(WRITE + DELIM + r + DELIM + reqid[r] + DELIM + t + DELIM + rk + DELIM + readval[r]), beb);
                } else {
                    trigger(new BebBroadcast(WRITE + DELIM + r + DELIM + reqid[r] + DELIM + (t + 1) + DELIM + i + DELIM + readval[r]), beb);
                }
            }
        }
    }

    private void checkWriteSetEvent() {
        for (int r = 0; r < registerNumber; r++) {
            if (writeSet.get(r).size() > all.size() / 2) {
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
    private int rank(Address self, Set<Address> all) {
        int rank = 0;
        for (Address addr : all) {
            if (self.equals(addr)) {
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

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ReadSetEntry other = (ReadSetEntry) obj;
            if (this.timestamp != other.timestamp) {
                return false;
            }
            if (this.rank != other.rank) {
                return false;
            }
            if (this.value != other.value) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + this.timestamp;
            hash = 11 * hash + this.rank;
            hash = 11 * hash + this.value;
            return hash;
        }
    }
}
