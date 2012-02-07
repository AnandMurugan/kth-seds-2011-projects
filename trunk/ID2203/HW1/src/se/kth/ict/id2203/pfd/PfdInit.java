/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfd;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public final class PfdInit extends Init {
    private final Set<Address> neighborSet;
    private final Address self;
    private final int heartbeatInterval;
    private final int checkInterval;

    public PfdInit(Set<Address> neighborSet, Address self, int heartbeatInterval, int checkInterval) {
        this.neighborSet = neighborSet;
        this.self = self;
        this.heartbeatInterval = heartbeatInterval;
        this.checkInterval = checkInterval;
    }

    /**
     * Gets the neighbor set.
     * 
     * @return the neighbor set
     */
    public final Set<Address> getNeighborSet() {
        return neighborSet;
    }

    /**
     * Gets the self.
     * 
     * @return the self
     */
    public final Address getSelf() {
        return self;
    }
    
    public final int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public final int getCheckInterval() {
        return checkInterval;
    }
}
