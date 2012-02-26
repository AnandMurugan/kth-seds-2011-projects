/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.uniform.paxos;

import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class PaxosUniformConsensusInit extends Init {
    private Address self;

    public PaxosUniformConsensusInit(Address self) {
        this.self = self;
    }

    public Address getSelf() {
        return self;
    }
}
