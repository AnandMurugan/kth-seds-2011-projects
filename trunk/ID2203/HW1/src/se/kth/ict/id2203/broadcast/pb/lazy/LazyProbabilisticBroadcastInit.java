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
    private final Set<Address> neighborSet;
    private final Address self;
    private final long seed;
    private final int fanout;
    private final float alpha;
    private final int delta;
    private final int maxRounds;

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

    public final Set<Address> getNeighborSet() {
        return neighborSet;
    }

    public final Address getSelf() {
        return self;
    }

    public final long getSeed() {
        return seed;
    }

    public final int getFanout() {
        return fanout;
    }

    public final float getAlpha() {
        return alpha;
    }

    public final int getDelta() {
        return delta;
    }

    public final int getMaxRounds() {
        return maxRounds;
    }
}
