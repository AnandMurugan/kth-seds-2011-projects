/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.best.basic;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class BasicBroadcastInit extends Init {
    private final Set<Address> neighborSet;
    private final Address self;

    public BasicBroadcastInit(Set<Address> neighborSet, Address self) {
        this.neighborSet = neighborSet;
        this.self = self;
    }

    public final Set<Address> getNeighborSet() {
        return neighborSet;
    }

    public final Address getSelf() {
        return self;
    }
    
}
