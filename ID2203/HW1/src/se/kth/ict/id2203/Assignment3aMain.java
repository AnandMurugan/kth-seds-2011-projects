/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.application.assignment3.Application3;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.broadcast.beb.basic.BasicBroadcast;
import se.kth.ict.id2203.broadcast.beb.basic.BasicBroadcastInit;
import se.kth.ict.id2203.fd.pfd.PerfectFailureDetector;
import se.kth.ict.id2203.fd.pfd.simple.SimplePerfectFailureDetector;
import se.kth.ict.id2203.fd.pfd.simple.SimplePerfectFailureDetectorInit;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.kth.ict.id2203.pp2p.delay.DelayLinkInit;
import se.kth.ict.id2203.registers.atomic.AtomicRegister;
import se.kth.ict.id2203.registers.atomic.riwc.ReadImposeWriteConsultAtomicRegister;
import se.kth.ict.id2203.registers.atomic.riwc.ReadImposeWriteConsultAtomicRegisterInit;
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
public class Assignment3aMain extends ComponentDefinition {
    static {
        PropertyConfigurator.configureAndWatch("log4j.properties");
    }
    //private final static int FANOUT = 3;
    //private final static float ALPHA = 0.5f;
    //private final static int DELTA = 5000;
    //private final static int MAX_ROUNDS = 2;
    private static int selfId;
    private static String commandScript;
    Topology topology = Topology.load(System.getProperty("topology"), selfId);

    public static void main(String[] args) {
        selfId = Integer.parseInt(args[0]);
        commandScript = args[1];

        Kompics.createAndStart(Assignment3aMain.class);
    }

    public Assignment3aMain() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component pp2p = create(DelayLink.class);
        Component pfd = create(SimplePerfectFailureDetector.class);
        Component beb = create(BasicBroadcast.class);
        Component riwc = create(ReadImposeWriteConsultAtomicRegister.class);
        Component app = create(Application3.class);

        // handle possible faults in the components
        subscribe(faultHandler, time.control());
        subscribe(faultHandler, network.control());
        subscribe(faultHandler, pp2p.control());
        subscribe(faultHandler, pfd.control());
        subscribe(faultHandler, beb.control());
        subscribe(faultHandler, riwc.control());
        subscribe(faultHandler, app.control());

        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);
        int heartbeatInterval = 1000;
        int checkInterval = 4000;
        int numRegisters = 1;

        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayLinkInit(topology), pp2p.control());
        trigger(new BasicBroadcastInit(neighborSet, self), beb.control());
        trigger(new SimplePerfectFailureDetectorInit(neighborSet, self, heartbeatInterval, checkInterval), pfd.control());
        trigger(new ReadImposeWriteConsultAtomicRegisterInit(neighborSet, self, numRegisters), riwc.control());
        trigger(new ApplicationInit(commandScript, self), app.control());

        // connect the components
        connect(app.required(AtomicRegister.class), riwc.provided(AtomicRegister.class));
        connect(app.required(Timer.class), time.provided(Timer.class));

        connect(riwc.required(BestEffortBroadcast.class), beb.provided(BestEffortBroadcast.class));
        connect(riwc.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        connect(riwc.required(PerfectFailureDetector.class), pfd.provided(PerfectFailureDetector.class));

        connect(beb.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));

        connect(pfd.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        connect(pfd.required(Timer.class), time.provided(Timer.class));

        connect(pp2p.required(Network.class), network.provided(Network.class));
        connect(pp2p.required(Timer.class), time.provided(Timer.class));
    }
    //handlers
    Handler<Fault> faultHandler = new Handler<Fault>() {
        @Override
        public void handle(Fault fault) {
            fault.getFault().printStackTrace(System.err);
        }
    };
}
