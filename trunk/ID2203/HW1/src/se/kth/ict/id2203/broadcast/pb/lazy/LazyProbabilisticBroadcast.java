/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import se.kth.ict.id2203.broadcast.pb.PbBroadcast;
import se.kth.ict.id2203.broadcast.pb.ProbabilisticBroadcast;
import se.kth.ict.id2203.broadcast.un.UnBroadcast;
import se.kth.ict.id2203.broadcast.un.UnreliableBroadcast;
import se.kth.ict.id2203.broadcast.un.simple.DataMessage;
import se.kth.ict.id2203.flp2p.FairLossPointToPointLink;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author Igor
 */
public class LazyProbabilisticBroadcast extends ComponentDefinition {
    //ports
    Negative<ProbabilisticBroadcast> pb = provides(ProbabilisticBroadcast.class);
    Positive<UnreliableBroadcast> un = requires(UnreliableBroadcast.class);
    Positive<FairLossPointToPointLink> flp2p = requires(FairLossPointToPointLink.class);
    Positive<Timer> timer = requires(Timer.class);
    //consts
    private final static String SN_DELIMITER = ";";
    //local variables
    private Address self;
    private Set<Address> neighborSet;
    private Map<Address, Integer> next;
    private int lsn;
    private List<DataMessage> pending;
    private List<DataMessage> stored;

    public LazyProbabilisticBroadcast() {
        subscribe(initHandler, control);
        subscribe(pbBroadcastHandler, pb);
    }
    //handlers
    Handler<LazyProbabilisticBroadcastInit> initHandler = new Handler<LazyProbabilisticBroadcastInit>() {
        @Override
        public void handle(LazyProbabilisticBroadcastInit event) {
            self = event.getSelf();
            neighborSet = event.getNeighborSet();

            next = new HashMap<Address, Integer>();
            for (Address p : neighborSet) {
                next.put(p, 1);
            }

            lsn = 0;

            pending = new ArrayList<DataMessage>();
            stored = new ArrayList<DataMessage>();
        }
    };
    Handler<PbBroadcast> pbBroadcastHandler = new Handler<PbBroadcast>() {
        @Override
        public void handle(PbBroadcast event) {
            ++lsn;
            trigger(new UnBroadcast(lsn + SN_DELIMITER + event.getMessage()), un); //piggybacking sn as a prefix to message
        }
    };
}
