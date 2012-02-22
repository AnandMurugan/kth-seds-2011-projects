/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import se.kth.ict.id2203.broadcast.best.BebBroadcast;
import se.kth.ict.id2203.broadcast.best.BestEffortBroadcast;
import se.kth.ict.id2203.fd.pfd.Crash;
import se.kth.ict.id2203.fd.pfd.PerfectFailureDetector;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.Pp2pSend;
import se.kth.ict.id2203.registers.atomic.AckMessage;
import se.kth.ict.id2203.registers.atomic.AtomicRegister;
import se.kth.ict.id2203.registers.atomic.ReadRequest;
import se.kth.ict.id2203.registers.atomic.ReadResponse;
import se.kth.ict.id2203.registers.atomic.WriteRequest;
import se.kth.ict.id2203.registers.atomic.WriteResponse;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author julio
 */
public class ReadImposeWriteConsult extends ComponentDefinition {
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
    private static final Logger logger = LoggerFactory.getLogger(ReadImposeWriteConsult.class);

    public ReadImposeWriteConsult() {
        subscribe(InitHandler, control);
        subscribe(ReadRequestHandler, nnar);
        subscribe(WriteRequestHandler, nnar);
        subscribe(WriteMessageHandler, beb);
        subscribe(AckMessageHandler, pp2p);
        subscribe(CrashHandler, pfd);

        aliveNodes = new HashSet<Address>();
    }
    Handler<RiwcInit> InitHandler = new Handler<RiwcInit>() {
        @Override
        public void handle(RiwcInit event) {
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
    Handler<ReadRequest> ReadRequestHandler = new Handler<ReadRequest>() {
        @Override
        public void handle(ReadRequest event) {
            int r = event.getRegister();
            reqId[r]++;
            reading[r] = true;
            writeSet.get(r).clear();
            readVal[r] = v[r];
            //logger.info("READ REQUEST: {}", reqId[r]);
            WriteMessage writeMsg = new WriteMessage(self, "", r, v[r], reqId[r], ts[r], mrank[r]);
            trigger(new BebBroadcast(writeMsg), beb);
        }
    };
    Handler<WriteRequest> WriteRequestHandler = new Handler<WriteRequest>() {
        @Override
        public void handle(WriteRequest event) {
            int r = event.getRegister();
            int val = event.getValue();
            reqId[r]++;
            writeSet.get(r).clear();
            //logger.info("WRITE REQUEST: {}", reqId[r]);
            WriteMessage writeMsg = new WriteMessage(self, "", r, val, reqId[r], ts[r] + 1, iRank);
            trigger(new BebBroadcast(writeMsg), beb);
        }
    };
    Handler<WriteMessage> WriteMessageHandler = new Handler<WriteMessage>() {
        @Override
        public void handle(WriteMessage event) {
            int r = event.getR();
            int timestamp = event.getTimestamp();
            int rank = event.getRank();

            if (timestamp > ts[r] || (timestamp == ts[r] && rank > mrank[r])) {
                v[r] = event.getV();
                ts[r] = timestamp;
                mrank[r] = rank;
            }
            //logger.debug("HANDLING WRITE (reqId): {}", reqId[r]);
            trigger(new Pp2pSend(event.getSource(), new AckMessage(self, r, event.getId())), pp2p);
        }
    };
    Handler<AckMessage> AckMessageHandler = new Handler<AckMessage>() {
        @Override
        public void handle(AckMessage event) {
            int r = event.getRegister();
            int id = event.getRequestId();
            //logger.debug("RECEIVING ACK FROM {}", event.getSource());
            //logger.debug("data:((reqId, id) : {},{}", reqId[r], id);
            if (id == reqId[r]) {
                writeSet.get(r).add(event.getSource());
            }

            checkReceivedAcks(r);
        }
    };
    Handler<Crash> CrashHandler = new Handler<Crash>() {
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
