/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203;

import java.util.Set;
import org.apache.log4j.PropertyConfigurator;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.application.assignment4.Application4;
import se.kth.ict.id2203.broadcast.beb.BestEffortBroadcast;
import se.kth.ict.id2203.broadcast.beb.basic.BasicBroadcast;
import se.kth.ict.id2203.broadcast.beb.basic.BasicBroadcastInit;
import se.kth.ict.id2203.consensus.abortable.AbortableConsensus;
import se.kth.ict.id2203.consensus.abortable.rw.ReadWriteAbortableConsensus;
import se.kth.ict.id2203.consensus.abortable.rw.ReadWriteAbortableConsensusInit;
import se.kth.ict.id2203.consensus.uniform.UniformConsensus;
import se.kth.ict.id2203.consensus.uniform.paxos.PaxosUniformConsensus;
import se.kth.ict.id2203.consensus.uniform.paxos.PaxosUniformConsensusInit;
import se.kth.ict.id2203.detectors.leader.eld.EventualLeaderDetector;
import se.kth.ict.id2203.detectors.leader.eld.failnoisy.FailNoisyEventualLeaderDetector;
import se.kth.ict.id2203.detectors.leader.eld.failnoisy.FailNoisyEventualLeaderDetectorInit;
import se.kth.ict.id2203.link.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.link.pp2p.delay.DelayLink;
import se.kth.ict.id2203.link.pp2p.delay.DelayLinkInit;
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
public class Assignment4Main extends ComponentDefinition {
    static {
        PropertyConfigurator.configureAndWatch("log4j.properties");
    }
    private static final long TIME_DELAY = 2000;
    private static final long DELTA = 500;
    private static int selfId;
    private static String commandScript;
    Topology topology = Topology.load(System.getProperty("topology"), selfId);

    public static void main(String[] args) {
        selfId = Integer.parseInt(args[0]);
        commandScript = args[1];

        Kompics.createAndStart(Assignment4Main.class);
    }

    public Assignment4Main() {
        // create components
        Component time = create(JavaTimer.class);
        Component network = create(MinaNetwork.class);
        Component pp2p = create(DelayLink.class);
        Component beb = create(BasicBroadcast.class);
        Component eld = create(FailNoisyEventualLeaderDetector.class);
        Component ac = create(ReadWriteAbortableConsensus.class);
        Component uc = create(PaxosUniformConsensus.class);
        Component app = create(Application4.class);

        // handle possible faults in the components
        subscribe(faultHandler, time.control());
        subscribe(faultHandler, network.control());
        subscribe(faultHandler, pp2p.control());
        subscribe(faultHandler, beb.control());
        subscribe(faultHandler, eld.control());
        subscribe(faultHandler, ac.control());
        subscribe(faultHandler, uc.control());
        subscribe(faultHandler, app.control());

        // initialize the components
        Address self = topology.getSelfAddress();
        Set<Address> neighborSet = topology.getNeighbors(self);
        Set<Address> all = topology.getAllAddresses();

        trigger(new MinaNetworkInit(self, 5), network.control());
        trigger(new DelayLinkInit(topology), pp2p.control());
        trigger(new BasicBroadcastInit(neighborSet, self), beb.control());
        trigger(new FailNoisyEventualLeaderDetectorInit(all, self, TIME_DELAY, DELTA), eld.control());
        trigger(new ReadWriteAbortableConsensusInit(self, all.size()), ac.control());
        trigger(new PaxosUniformConsensusInit(self), uc.control());
        trigger(new ApplicationInit(commandScript), app.control());

        // connect the components
        connect(app.required(UniformConsensus.class), uc.provided(UniformConsensus.class));
        connect(app.required(Timer.class), time.provided(Timer.class));

        connect(uc.required(BestEffortBroadcast.class), beb.provided(BestEffortBroadcast.class));
        connect(uc.required(AbortableConsensus.class), ac.provided(AbortableConsensus.class));
        connect(uc.required(EventualLeaderDetector.class), eld.provided(EventualLeaderDetector.class));

        connect(ac.required(BestEffortBroadcast.class), beb.provided(BestEffortBroadcast.class));
        connect(ac.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));

        connect(beb.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));

        connect(eld.required(PerfectPointToPointLink.class), pp2p.provided(PerfectPointToPointLink.class));
        connect(eld.required(Timer.class), time.provided(Timer.class));

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
