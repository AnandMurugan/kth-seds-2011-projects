/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure.pfd.simple;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class SimplePerfectFailureDetectorInit extends Init {
    private Set<Address> neighborSet;
    private Address self;
    private int heartbeatInterval;
    private int checkInterval;

    public SimplePerfectFailureDetectorInit(Set<Address> neighborSet, Address self, int heartbeatInterval, int checkInterval) {
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
    public Set<Address> getNeighborSet() {
        return neighborSet;
    }

    /**
     * Gets the self.
     *
     * @return the self
     */
    public Address getSelf() {
        return self;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public int getCheckInterval() {
        return checkInterval;
    }
}
