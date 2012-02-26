/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.broadcast.beb.BebBroadcast;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.detectors.failure.pfd.Crash;
import se.kth.ict.id2203.detectors.failure.pfd.PerfectFailureDetector;
import se.kth.ict.id2203.link.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.link.pp2p.Pp2pSend;
import se.kth.ict.id2203.registers.atomic.*;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class ReadImposeWriteConsultAtomicRegister extends ComponentDefinition {
    Negative<AtomicRegister> nnar = provides(AtomicRegister.class);
    Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
    Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
    Positive<PerfectFailureDetector> pfd = requires(PerfectFailureDetector.class);
    private Set<Address> neighborNodes;
    private Set<Address> aliveNodes;
    private Address self;
    private int numRegisters;
    private Map<Integer, Set<Address>> writeSet;
    private boolean[] reading;
    private int[] mrank, reqId, readVal, v, ts;
    private int iRank;
    private static final Logger logger = LoggerFactory.getLogger(ReadImposeWriteConsultAtomicRegister.class);

    public ReadImposeWriteConsultAtomicRegister() {
        subscribe(initHandler, control);
        subscribe(readRequestHandler, nnar);
        subscribe(writeRequestHandler, nnar);
        subscribe(writeMessageHandler, beb);
        subscribe(ackMessageHandler, pp2p);
        subscribe(crashHandler, pfd);

        aliveNodes = new HashSet<Address>();
    }
    Handler<ReadImposeWriteConsultAtomicRegisterInit> initHandler = new Handler<ReadImposeWriteConsultAtomicRegisterInit>() {
        @Override
        public void handle(ReadImposeWriteConsultAtomicRegisterInit event) {
            neighborNodes = event.getNeighborSet();
            aliveNodes.addAll(neighborNodes);
            self = event.getSelf();
            aliveNodes.add(self);
            numRegisters = event.getNumRegisters();
            writeSet = new HashMap<Integer, Set<Address>>();
            reading = new boolean[numRegisters];
            mrank = new int[numRegisters];
            reqId = new int[numRegisters];
            readVal = new int[numRegisters];
            v = new int[numRegisters];
            ts = new int[numRegisters];
            iRank = self.getId();
            for (int i = 0; i < numRegisters; i++) {
                writeSet.put(i, new HashSet<Address>());
                reading[i] = false;
                mrank[i] = 0;
                reqId[i] = 0;
                readVal[i] = 0;
                v[i] = 0;
                ts[i] = 0;
            }
        }
    };
    Handler<ReadRequest> readRequestHandler = new Handler<ReadRequest>() {
        @Override
        public void handle(ReadRequest event) {
            int r = event.getRegister();
            reqId[r]++;
            reading[r] = true;
            writeSet.get(r).clear();
            readVal[r] = v[r];
            //logger.info("READ REQUEST: {}", reqId[r]);
            WriteMessage writeMsg = new WriteMessage(self, r, reqId[r], ts[r], mrank[r], v[r]);
            trigger(new BebBroadcast(writeMsg), beb);
        }
    };
    Handler<WriteRequest> writeRequestHandler = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest event) {
            int r = event.getRegister();
            int val = event.getValue();
            reqId[r]++;
            writeSet.get(r).clear();
            //logger.info("WRITE REQUEST: {}", reqId[r]);
            WriteMessage writeMsg = new WriteMessage(self, r, reqId[r], ts[r] + 1, iRank, val);
            trigger(new BebBroadcast(writeMsg), beb);
        }
    };
    Handler<WriteMessage> writeMessageHandler = new Handler<WriteMessage>() {
        @Override
        public void handle(WriteMessage event) {
            int r = event.getRegister();
            int timestamp = event.getTimestamp();
            int rank = event.getRank();

            if (timestamp > ts[r] || (timestamp == ts[r] && rank > mrank[r])) {
                v[r] = event.getValue();
                ts[r] = timestamp;
                mrank[r] = rank;
            }
            logger.debug("HANDLING WRITE (reqId): {}", reqId[r]);
            trigger(new Pp2pSend(event.getSource(), new AckMessage(self, r, event.getRequestId())), pp2p);
        }
    };
    Handler<AckMessage> ackMessageHandler = new Handler<AckMessage>() {
        @Override
        public void handle(AckMessage event) {
            int r = event.getRegister();
            int id = event.getRequestId();
            logger.debug("RECEIVING ACK FROM {}", event.getSource());
            logger.debug("data:((reqId, id) : {},{}", reqId[r], id);
            if (id == reqId[r]) {
                writeSet.get(r).add(event.getSource());
            }

            checkReceivedAcks(r);
        }
    };
    Handler<Crash> crashHandler = new Handler<Crash>() {
        @Override
        public void handle(Crash event) {
            logger.debug("Node crashed: {}", event.getNodeCrashed());
            aliveNodes.remove(event.getNodeCrashed());
            for (int i = 0; i < numRegisters; i++) {
                checkReceivedAcks(i);
            }
        }
    };

    private boolean checkReceivedAcks(int r) {
        //logger.debug("Alive nodes size: {}", aliveNodes.size());
        //logger.debug("Writeset size: {}", writeSet.get(r).size());

        for (Address addr : aliveNodes) {

            if (!writeSet.get(r).contains(addr)) {
                return false;
            }
        }

        if (reading[r]) {
            reading[r] = false;
            trigger(new ReadResponse(r, readVal[r]), nnar);
        } else {
            trigger(new WriteResponse(r), nnar);
        }

        return true;
    }
}
