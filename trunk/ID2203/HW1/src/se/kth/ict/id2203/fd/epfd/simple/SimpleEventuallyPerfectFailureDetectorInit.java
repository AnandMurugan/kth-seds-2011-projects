/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.fd.epfd.simple;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class SimpleEventuallyPerfectFailureDetectorInit extends Init {
    private int timeDelay;
    private int delta;
    private Set<Address> neighborSet;
    private Address self;

    public SimpleEventuallyPerfectFailureDetectorInit(int timeDelay, int delta, Set<Address> neighborSet, Address self) {
        this.timeDelay = timeDelay;
        this.delta = delta;
        this.neighborSet = neighborSet;
        this.self = self;
    }

    public int getDelta() {
        return delta;
    }

    public Set<Address> getNeighborSet() {
        return neighborSet;
    }

    public int getTimeDelay() {
        return timeDelay;
    }

    public Address getSelf() {
        return self;
    }
}
