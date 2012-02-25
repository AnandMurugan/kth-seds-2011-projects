/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.assignment2.Application2;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.broadcast.pb.ProbabilisticBroadcast;
import se.kth.ict.id2203.broadcast.pb.lazy.LazyProbabilisticBroadcast;
import se.kth.ict.id2203.broadcast.pb.lazy.LazyProbabilisticBroadcastInit;
import se.kth.ict.id2203.broadcast.un.UnreliableBroadcast;
import se.kth.ict.id2203.broadcast.un.simple.SimpleUnreliableBroadcast;
import se.kth.ict.id2203.broadcast.un.simple.SimpleUnreliableBroadcastInit;
import se.kth.ict.id2203.links.flp2p.FairLossPointToPointLink;
import se.kth.ict.id2203.links.flp2p.delay.DelayDropLink;
import se.kth.ict.id2203.links.flp2p.delay.DelayDropLinkInit;
import se.sics.kompics.*;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.Topology;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.mina.MinaNetwork;
import se.sics.kompics.network.mina.MinaNetworkInit;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;

/**
 *
 * @author Igor
 */
public class Assignment2Main extends ComponentDefinition {
    static {
        PropertyConfigurator.configureAndWatch("log4j.properties");
    }
    private final static int FANOUT = 3;
    private final static float ALPHA = 0.5f;
    private final static int DELTA = 5000;
    private final static int MAX_ROUNDS = 2;
    private static int selfId;
    private static String commandScript;
    Topology topology = Topology.load(System.getProperty("topology"), selfId);

    public static void main(String[] args) {
        selfId = Integer.parseInt(args[0]);
        commandScript = args[1];

        Kompics.createAndStart(Assignment2Main.class);
    }

    public Assignment2Main() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component flp2p = create(DelayDropLink.class);
        Component un = create(SimpleUnreliableBroadcast.class);
        Component pb = create(LazyProbabilisticBroadcast.class);
        Component app = create(Application2.class);

        // handle possible faults in the components
        subscribe(faultHandler, time.control());
        subscribe(faultHandler, network.control());
        subscribe(faultHandler, flp2p.control());
        subscribe(faultHandler, un.control());
        subscribe(faultHandler, pb.control());
        subscribe(faultHandler, app.control());

        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);

        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayDropLinkInit(topology, System.nanoTime()), flp2p.control());
        trigger(new SimpleUnreliableBroadcastInit(neighborSet, self), un.control());
        trigger(new LazyProbabilisticBroadcastInit(neighborSet, self, System.nanoTime(), FANOUT, ALPHA, DELTA, MAX_ROUNDS), pb.control());
        trigger(new ApplicationInit(commandScript, self), app.control());

        // connect the components
        connect(app.required(ProbabilisticBroadcast.class), pb.provided(ProbabilisticBroadcast.class));
        connect(app.required(Timer.class), time.provided(Timer.class));

        connect(pb.required(UnreliableBroadcast.class), un.provided(UnreliableBroadcast.class));
        connect(pb.required(FairLossPointToPointLink.class), flp2p.provided(FairLossPointToPointLink.class));
        connect(pb.required(Timer.class), time.provided(Timer.class));

        connect(un.required(FairLossPointToPointLink.class), flp2p.provided(FairLossPointToPointLink.class));

        connect(flp2p.required(Network.class), network.provided(Network.class));
        connect(flp2p.required(Timer.class), time.provided(Timer.class));
    }
    //handlers
    Handler<Fault> faultHandler = new Handler<Fault>() {
        @Override
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
