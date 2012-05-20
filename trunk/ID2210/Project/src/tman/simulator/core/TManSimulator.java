package tman.simulator.core;

import common.configuration.Configuration;
import common.configuration.CyclonConfiguration;
import common.configuration.TManConfiguration;
import common.simulation.SimulatorPort;
import common.simulation.GenerateReport;
import common.simulation.PeerFail;
import common.simulation.ConsistentHashtable;
import common.simulation.PeerJoin;
import java.math.BigInteger;
import java.util.HashMap;
import common.peer.JoinPeer;
import common.peer.PeerAddress;
import common.peer.PeerPort;
import common.simulation.SimulatorInit;

import se.peertv.ispFriendliness.AsIpGenerator;
import se.sics.kompics.ChannelFilter;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.Stop;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Message;
import se.sics.kompics.network.Network;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;

import java.net.InetAddress;
import java.util.Random;
import tman.system.peer.Peer;
import tman.system.peer.PeerInit;
import tman.simulator.snapshot.Snapshot;

public final class TManSimulator extends ComponentDefinition {
	Positive<SimulatorPort> simulator = positive(SimulatorPort.class);
	Positive<Network> network = positive(Network.class);
	Positive<Timer> timer = positive(Timer.class);

	private final HashMap<BigInteger, Component> peers;
	private final HashMap<BigInteger, PeerAddress> peersAddress;
	
	private BootstrapConfiguration bootstrapConfiguration;
	private CyclonConfiguration cyclonConfiguration;
	private TManConfiguration tmanConfiguration;

	private int peerIdSequence;
	private BigInteger cyclonIdentifierSpaceSize;
	private ConsistentHashtable<BigInteger> cyclonView;
    private AsIpGenerator ipGenerator;
    private Random rnd;

//-------------------------------------------------------------------	
	public TManSimulator() {
		peers = new HashMap<BigInteger, Component>();
		peersAddress = new HashMap<BigInteger, PeerAddress>();
		cyclonView = new ConsistentHashtable<BigInteger>();

        rnd = new Random(10);
        ipGenerator = AsIpGenerator.getInstance(rnd.nextInt());
                
		subscribe(handleInit, control);

		subscribe(handleGenerateReport, timer);
		subscribe(handlePeerJoin, simulator);
		subscribe(handlePeerFail, simulator);
	}

//-------------------------------------------------------------------	
	Handler<SimulatorInit> handleInit = new Handler<SimulatorInit>() {
		public void handle(SimulatorInit init) {
			peers.clear();
			peerIdSequence = 0;

			bootstrapConfiguration = init.getBootstrapConfiguration();
			cyclonConfiguration = init.getCyclonConfiguration();
			tmanConfiguration = init.getTmanConfiguration();

			cyclonIdentifierSpaceSize = cyclonConfiguration.getIdentifierSpaceSize();
			
			// generate periodic report
			int snapshotPeriod = Configuration.SNAPSHOT_PERIOD;
			SchedulePeriodicTimeout spt = new SchedulePeriodicTimeout(snapshotPeriod, snapshotPeriod);
			spt.setTimeoutEvent(new GenerateReport(spt));
			trigger(spt, timer);
		}
	};

//-------------------------------------------------------------------	
	Handler<PeerJoin> handlePeerJoin = new Handler<PeerJoin>() {
		public void handle(PeerJoin event) {
			int num = event.getNum();
			BigInteger id = event.getPeerId();

			// join with the next id if this id is taken
			BigInteger successor = cyclonView.getNode(id);

			while (successor != null && successor.equals(id)) {
				id = id.add(BigInteger.ONE).mod(cyclonIdentifierSpaceSize);
				successor = cyclonView.getNode(id);
			}

			Component newPeer = createAndStartNewPeer(id, num);
			cyclonView.addNode(id);

			trigger(new JoinPeer(id), newPeer.getPositive(PeerPort.class));
		}
	};
	
//-------------------------------------------------------------------	
	Handler<PeerFail> handlePeerFail = new Handler<PeerFail>() {
		public void handle(PeerFail event) {
			BigInteger id = cyclonView.getNode(event.getCyclonId());

			if (cyclonView.size() == 0) {
				System.err.println("Empty network");
				return;
			}

			cyclonView.removeNode(id);
			stopAndDestroyPeer(id);
		}
	};	

//-------------------------------------------------------------------	
	Handler<GenerateReport> handleGenerateReport = new Handler<GenerateReport>() {
		public void handle(GenerateReport event) {
			Snapshot.report();
		}
	};

//-------------------------------------------------------------------	
	private final Component createAndStartNewPeer(BigInteger id, int num) {
		Component peer = create(Peer.class);
		int peerId = ++peerIdSequence;
		InetAddress ip = ipGenerator.generateIP();
		Address address = new Address(ip, 8058, peerId);
//                        new Address(peer0Address.getIp(), peer0Address.getPort(), peerId);

		PeerAddress peerAddress = new PeerAddress(address, id);
		
		connect(network, peer.getNegative(Network.class), new MessageDestinationFilter(address));
		connect(timer, peer.getNegative(Timer.class));

		trigger(new PeerInit(peerAddress, bootstrapConfiguration, cyclonConfiguration, tmanConfiguration), peer.getControl());

		trigger(new Start(), peer.getControl());
		peers.put(id, peer);
		peersAddress.put(id, peerAddress);

		return peer;
	}

//-------------------------------------------------------------------	
	private final void stopAndDestroyPeer(BigInteger id) {
		Component peer = peers.get(id);

		trigger(new Stop(), peer.getControl());

		disconnect(network, peer.getNegative(Network.class));
		disconnect(timer, peer.getNegative(Timer.class));
		
		Snapshot.removePeer(peersAddress.get(id));

		peers.remove(id);
		peersAddress.remove(id);

		destroy(peer);
	}

//-------------------------------------------------------------------	
	private final static class MessageDestinationFilter extends
			ChannelFilter<Message, Address> {
		public MessageDestinationFilter(Address address) {
			super(Message.class, address, true);
		}

		public Address getValue(Message event) {
			return event.getDestination();
		}
	}
}
