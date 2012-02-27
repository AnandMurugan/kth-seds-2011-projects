/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.uniform.paxos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import se.kth.ict.id2203.broadcast.beb.BebBroadcast;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.consensus.abortable.AbortableConsensus;
import se.kth.ict.id2203.consensus.abortable.AcDecide;
import se.kth.ict.id2203.consensus.abortable.AcPropose;
import se.kth.ict.id2203.consensus.uniform.UcDecide;
import se.kth.ict.id2203.consensus.uniform.UcPropose;
import se.kth.ict.id2203.consensus.uniform.UniformConsensus;
import se.kth.ict.id2203.detectors.leader.eld.EventualLeaderDetector;
import se.kth.ict.id2203.detectors.leader.eld.Trust;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class PaxosUniformConsensus extends ComponentDefinition {
    //ports
    Negative<UniformConsensus> uc = provides(UniformConsensus.class);
    Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
    Positive<AbortableConsensus> ac = requires(AbortableConsensus.class);
    Positive<EventualLeaderDetector> eld = requires(EventualLeaderDetector.class);
    //local variables
    private Address self;
    private Set<Integer> seenIds;
    private boolean leader;
    private Map<Integer, Object> proposal;
    private Map<Integer, Boolean> proposed;
    private Map<Integer, Boolean> decided;

    public PaxosUniformConsensus() {
        subscribe(initHandler, control);
        subscribe(trustHandler, eld);
        subscribe(ucProposeHandler, uc);
        subscribe(acDecideHandler, ac);
        subscribe(decidedMessageHandler, beb);
    }
    //handlers
    Handler<PaxosUniformConsensusInit> initHandler = new Handler<PaxosUniformConsensusInit>() {
        @Override
        public void handle(PaxosUniformConsensusInit event) {
            self = event.getSelf();

            seenIds = new TreeSet<Integer>();
            leader = false;
            
            proposal = new HashMap<Integer, Object>();
            proposed = new HashMap<Integer, Boolean>();
            decided = new HashMap<Integer, Boolean>();
        }
    };
    Handler<Trust> trustHandler = new Handler<Trust>() {
        @Override
        public void handle(Trust event) {
            Address p = event.getLeader();

            if (p.equals(self)) {
                leader = true;
                for (int id : seenIds) {
                    tryPropose(id);
                }
            } else {
                leader = false;
            }
        }
    };
    Handler<UcPropose> ucProposeHandler = new Handler<UcPropose>() {
        @Override
        public void handle(UcPropose event) {
            int id = event.getId();
            Object v = event.getValue();

            initInstance(id);
            proposal.put(id, v);
            tryPropose(id);
        }
    };
    Handler<AcDecide> acDecideHandler = new Handler<AcDecide>() {
        @Override
        public void handle(AcDecide event) {
            int id = event.getId();
            Object result = event.getValue();

            if (result != null) {
                trigger(new BebBroadcast(new DecidedMessage(self, id, result)), beb);
            }
        }
    };
    Handler<DecidedMessage> decidedMessageHandler = new Handler<DecidedMessage>() {
        @Override
        public void handle(DecidedMessage event) {
            int id = event.getId();
            Object v = event.getValue();

            initInstance(id);
            if (!decided.get(id)) {
                decided.put(id, true);
                trigger(new UcDecide(id, v), uc);
            }
        }
    };

    //procedures and functions
    private void initInstance(int id) {
        if (!seenIds.contains(id)) {
            proposal.put(id, null);
            proposed.put(id, false);
            decided.put(id, false);
            seenIds.add(id);
        }
    }

    private void tryPropose(int id) {
        if (leader && !proposed.get(id) && proposal.get(id) != null) {
            proposed.put(id, true);
            trigger(new AcPropose(id, proposal.get(id)), ac);
        }
    }
}
