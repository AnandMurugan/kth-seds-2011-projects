/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwcm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.registers.atomic.AtomicRegister;
import se.kth.ict.id2203.registers.atomic.ReadRequest;
import se.kth.ict.id2203.registers.atomic.WriteRequest;
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
    //local variables
    private Address self;
    private Set<Address> neighbors;
    private Set<Address> all;
    private int i;
    private int registerNumber;
    private List<Set<Address>> writeSet;
    private List<Set<Address>> readSet;
    private boolean[] reading;
    private int[] reqid;
    private int[] v;
    private int[] ts;
    private int[] mrank;

    public ReadImposeWriteConsultMajorityAtomicRegister() {
        subscribe(initHandler, control);
        subscribe(readRequestHandler, nnar);
        subscribe(writeRequestHandler, nnar);
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
            readSet = new ArrayList<Set<Address>>(registerNumber);
            reading = new boolean[registerNumber];
            reqid = new int[registerNumber];
            v = new int[registerNumber];
            ts = new int[registerNumber];
            mrank = new int[registerNumber];

            i = rank(self, all);
            for (int j = 0; j < registerNumber; j++) {
                writeSet.add(new HashSet<Address>());
                readSet.add(new HashSet<Address>());
            }
        }
    };
    Handler<ReadRequest> readRequestHandler = new Handler<ReadRequest>() {
        @Override
        public void handle(ReadRequest e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    Handler<WriteRequest> writeRequestHandler = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    //procedures and functions
    private int rank(Address self, Set<Address> all) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
