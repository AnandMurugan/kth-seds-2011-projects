/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un.simple;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class SimpleUnreliableBroadcastInit extends Init {
    private Set<Address> neighborSet;
    private Address self;

    public SimpleUnreliableBroadcastInit(Set<Address> neighborSet, Address self) {
        this.neighborSet = neighborSet;
        this.self = self;
    }

    public Set<Address> getNeighborSet() {
        return neighborSet;
    }

    public Address getSelf() {
        return self;
    }
}
