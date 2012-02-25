/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.leader.eld.failnoisy;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class FailNoisyEventualLeaderDetectorInit extends Init {
    private Set<Address> all;
    private Address self;
    private long timeDelay;
    private long delta;

    public FailNoisyEventualLeaderDetectorInit(Set<Address> all, Address self, long timeDelay, long delta) {
        this.all = all;
        this.self = self;
        this.timeDelay = timeDelay;
        this.delta = delta;
    }

    public Set<Address> getAll() {
        return all;
    }

    public long getTimeDelay() {
        return timeDelay;
    }

    public Address getSelf() {
        return self;
    }

    public long getDelta() {
        return delta;
    }
}
