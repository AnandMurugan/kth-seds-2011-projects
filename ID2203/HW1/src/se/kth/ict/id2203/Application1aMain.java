/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.Application1a;
import se.kth.ict.id2203.application.Application1aInit;
import se.kth.ict.id2203.pfd.my.MyPerfectFailureDetector;
import se.kth.ict.id2203.pfd.PerfectFailureDetector;
import se.kth.ict.id2203.pfd.my.MyPerfectFailureDetectorInit;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.kth.ict.id2203.pp2p.delay.DelayLinkInit;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Fault;
import se.sics.kompics.Handler;
import se.sics.kompics.Kompics;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.Topology;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.mina.MinaNetwork;
import se.sics.kompics.network.mina.MinaNetworkInit;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;

/**
 *
 * @author julio
 */
public class Application1aMain extends ComponentDefinition {
    static {
        PropertyConfigurator.configureAndWatch("log4j.properties");
    }
    private static int selfId;
    private static String commandScript;
    Topology topology = Topology.load(System.getProperty("topology"), selfId);

    public static void main(String[] args) {
        selfId = Integer.parseInt(args[0]);
        commandScript = args[1];
        Kompics.createAndStart(Application1aMain.class);
    }

    public Application1aMain() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component pp2p = create(DelayLink.class);
        Component pfd = create(MyPerfectFailureDetector.class);
        Component app = create(Application1a.class);

        // handle possible faults in the components
        subscribe(handleFault, time.control());
        subscribe(handleFault, network.control());
        subscribe(handleFault, pp2p.control());
        subscribe(handleFault, app.control());
        subscribe(handleFault, pfd.control());
        
        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);
        int heartbeatInterval = 1000;
        int checkInterval = 4000;

        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayLinkInit(topology), pp2p.control());
        trigger(new MyPerfectFailureDetectorInit(neighborSet, self, heartbeatInterval, checkInterval), pfd.control());
        trigger(new Application1aInit(commandScript, neighborSet, self), app.control());

        // connect the components
        connect(app.required(PerfectFailureDetector.class), pfd.provided(PerfectFailureDetector.class));
        connect(app.required(Timer.class), time.provided(Timer.class));

        connect(pfd.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        connect(pfd.required(Timer.class), time.provided(Timer.class));

        connect(pp2p.required(Timer.class), time.provided(Timer.class));
        connect(pp2p.required(Network.class), network.provided(Network.class));
    }
    Handler<Fault> handleFault = new Handler<Fault>() {
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
