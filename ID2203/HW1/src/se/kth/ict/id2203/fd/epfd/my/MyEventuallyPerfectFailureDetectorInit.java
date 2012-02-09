/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.fd.epfd.my;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class MyEventuallyPerfectFailureDetectorInit extends Init {
    private final int timeDelay;
    private final int delta;
    private final Set<Address> neighborSet;
    private Address self;

    public MyEventuallyPerfectFailureDetectorInit(int timeDelay, int delta, Set<Address> neighborSet, Address self) {
        this.timeDelay = timeDelay;
        this.delta = delta;
        this.neighborSet = neighborSet;
        this.self = self;
    }

    public final int getDelta() {
        return delta;
    }

    public final Set<Address> getNeighborSet() {
        return neighborSet;
    }

    public final int getTimeDelay() {
        return timeDelay;
    }

    public final Address getSelf() {
        return self;
    }
}
