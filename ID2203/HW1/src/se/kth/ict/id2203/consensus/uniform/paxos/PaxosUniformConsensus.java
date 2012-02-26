/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.uniform.paxos;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.consensus.abortable.AbortableConsensus;
import se.kth.ict.id2203.consensus.abortable.AcDecide;
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
        subscribe(decidedHandler, beb);
    }
    //handlers
    Handler<PaxosUniformConsensusInit> initHandler = new Handler<PaxosUniformConsensusInit>() {
        @Override
        public void handle(PaxosUniformConsensusInit event) {
            self = event.getSelf();

            seenIds = new TreeSet<Integer>();
            leader = false;
        }
    };
    Handler<Trust> trustHandler = new Handler<Trust>() {
        @Override
        public void handle(Trust event) {
        }
    };
    Handler<UcPropose> ucProposeHandler = new Handler<UcPropose>() {
        @Override
        public void handle(UcPropose event) {
        }
    };
    Handler<AcDecide> acDecideHandler = new Handler<AcDecide>() {
        @Override
        public void handle(AcDecide event) {
        }
    };
    Handler<PaxosUniformConsensusInit> decidedHandler = new Handler<PaxosUniformConsensusInit>() {
        @Override
        public void handle(PaxosUniformConsensusInit event) {
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
}
