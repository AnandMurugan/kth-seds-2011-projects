/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.Application3;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.broadcast.beb.basic.BasicBroadcast;
import se.kth.ict.id2203.broadcast.beb.basic.BasicBroadcastInit;
import se.kth.ict.id2203.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.pp2p.delay.DelayLink;
import se.kth.ict.id2203.pp2p.delay.DelayLinkInit;
import se.kth.ict.id2203.registers.atomic.AtomicRegister;
import se.kth.ict.id2203.registers.atomic.riwcm.ReadImposeWriteConsultMajorityAtomicRegister;
import se.kth.ict.id2203.registers.atomic.riwcm.ReadImposeWriteConsultMajorityAtomicRegisterInit;
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
public class Assignment3bMain extends ComponentDefinition {
    static {
        PropertyConfigurator.configureAndWatch("log4j.properties");
    }
    private final static int REGISTER_NUMBER = 5;
    private static int selfId;
    private static String commandScript;
    Topology topology = Topology.load(System.getProperty("topology"), selfId);

    public static void main(String[] args) {
        selfId = Integer.parseInt(args[0]);
        commandScript = args[1];

        Kompics.createAndStart(Assignment3bMain.class);
    }

    public Assignment3bMain() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component pp2p = create(DelayLink.class);
        Component beb = create(BasicBroadcast.class);
        Component nnar = create(ReadImposeWriteConsultMajorityAtomicRegister.class);
        Component app = create(Application3.class);

        // handle possible faults in the components
        subscribe(faultHandler, time.control());
        subscribe(faultHandler, network.control());
        subscribe(faultHandler, pp2p.control());
        subscribe(faultHandler, beb.control());
        subscribe(faultHandler, nnar.control());
        subscribe(faultHandler, app.control());

        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);
        Set<Address> all = topology.getAllAddresses();

        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayLinkInit(topology), pp2p.control());
        trigger(new BasicBroadcastInit(neighborSet, self), beb.control());
        trigger(new ReadImposeWriteConsultMajorityAtomicRegisterInit(all, self, REGISTER_NUMBER), nnar.control());
        trigger(new ApplicationInit(commandScript), app.control());

        // connect the components
        connect(app.required(AtomicRegister.class), nnar.provided(AtomicRegister.class));
        connect(app.required(Timer.class), time.provided(Timer.class));

        connect(nnar.required(BestEffortBroadcast.class), beb.provided(BestEffortBroadcast.class));
        connect(nnar.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));

        connect(beb.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));

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
