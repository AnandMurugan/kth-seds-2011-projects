/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class LazyProbabilisticBroadcastInit extends Init {
    private Set<Address> neighborSet;
    private Address self;
    private long seed;
    private int fanout;
    private float alpha;
    private int delta;
    private int maxRounds;

    public LazyProbabilisticBroadcastInit(Set<Address> neighborSet, Address self,
            long seed, int fanout, float alpha, int delta, int maxRounds) {
        this.neighborSet = neighborSet;
        this.self = self;
        this.seed = seed;
        this.fanout = fanout;
        this.alpha = alpha;
        this.delta = delta;
        this.maxRounds = maxRounds;
    }

    public Set<Address> getNeighborSet() {
        return neighborSet;
    }

    public Address getSelf() {
        return self;
    }

    public long getSeed() {
        return seed;
    }

    public int getFanout() {
        return fanout;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getDelta() {
        return delta;
    }

    public int getMaxRounds() {
        return maxRounds;
    }
}
