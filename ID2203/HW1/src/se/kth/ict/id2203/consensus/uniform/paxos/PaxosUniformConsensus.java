/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.uniform.paxos;

import java.util.*;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.consensus.abortable.AbortableConsensus;
import se.kth.ict.id2203.consensus.uniform.UniformConsensus;
import se.kth.ict.id2203.detectors.leader.eld.EventualLeaderDetector;
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

    public PaxosUniformConsensus() {
        subscribe(initHandler, control);
    }
    //handlers
    Handler<PaxosUniformConsensusInit> initHandler = new Handler<PaxosUniformConsensusInit>() {
        @Override
        public void handle(PaxosUniformConsensusInit event) {
            self = event.getSelf();
        }
    };

    //procedures and functions
    private void initInstance(int id) {
    }
}
