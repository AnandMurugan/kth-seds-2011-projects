/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.assignment1.Application1b;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.detectors.failure.epfd.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.detectors.failure.epfd.simple.SimpleEventuallyPerfectFailureDetectorInit;
import se.kth.ict.id2203.detectors.failure.epfd.simple.SimpleEventuallyPerfectFailureDetector_FairLoss;
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
public class Assignment1bMain_FairLoss extends ComponentDefinition {
    static {
        PropertyConfigurator.configureAndWatch("log4j.properties");
    }
    private static final int TIME_DELAY = 1000;
    private static final int DELTA = 500;
    private static int selfId;
    private static String commandScript;
    Topology topology = Topology.load(System.getProperty("topology"), selfId);
    
    public static void main(String[] args) {
        selfId = Integer.parseInt(args[0]);
        commandScript = args[1];
        
        Kompics.createAndStart(Assignment1bMain_FairLoss.class);
    }
    
    public Assignment1bMain_FairLoss() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component flp2p = create(DelayDropLink.class);
        Component epfd = create(SimpleEventuallyPerfectFailureDetector_FairLoss.class);
        Component app = create(Application1b.class);

        // handle possible faults in the components
        subscribe(faultHandler, time.control());
        subscribe(faultHandler, network.control());
        subscribe(faultHandler, flp2p.control());
        subscribe(faultHandler, epfd.control());
        subscribe(faultHandler, app.control());

        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);
        
        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayDropLinkInit(topology, System.nanoTime()), flp2p.control());
        trigger(new SimpleEventuallyPerfectFailureDetectorInit(TIME_DELAY, DELTA, neighborSet, self), epfd.control());
        trigger(new ApplicationInit(commandScript), app.control());

        // connect the components
        connect(app.required(EventuallyPerfectFailureDetector.class), epfd.provided(EventuallyPerfectFailureDetector.class));
        connect(app.required(Timer.class), time.provided(Timer.class));
        
        connect(epfd.required(FairLossPointToPointLink.class), flp2p.provided(FairLossPointToPointLink.class));
        connect(epfd.required(Timer.class), time.provided(Timer.class));
        
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
