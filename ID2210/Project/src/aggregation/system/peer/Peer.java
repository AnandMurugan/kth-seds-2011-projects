package aggregation.system.peer;

import common.peer.PeerPort;
import common.peer.JoinPeer;
import java.util.LinkedList;
import java.util.Set;

import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.network.Network;
import se.sics.kompics.p2p.bootstrap.BootstrapCompleted;
import se.sics.kompics.p2p.bootstrap.BootstrapRequest;
import se.sics.kompics.p2p.bootstrap.BootstrapResponse;
import se.sics.kompics.p2p.bootstrap.P2pBootstrap;
import se.sics.kompics.p2p.bootstrap.PeerEntry;
import se.sics.kompics.p2p.bootstrap.client.BootstrapClient;
import se.sics.kompics.p2p.bootstrap.client.BootstrapClientInit;
import se.sics.kompics.timer.Timer;

import aggregation.simulator.snapshot.Snapshot;
import common.peer.PeerAddress;
import aggregation.system.peer.aggregation.Aggregation;
import aggregation.system.peer.aggregation.AggregationInit;
import common.configuration.AggregationConfiguration;
import common.configuration.CyclonConfiguration;
import cyclon.system.peer.cyclon.*;


public final class Peer extends ComponentDefinition {
	
	Negative<PeerPort> peerPort = negative(PeerPort.class);
	Positive<Network> network = positive(Network.class);
	Positive<Timer> timer = positive(Timer.class);

	private Component cyclon, aggregation, bootstrap;
	private int num;
	private Address self;
	private PeerAddress peerSelf;
	private int bootstrapRequestPeerCount;
	private boolean bootstrapped;
	private AggregationConfiguration aggregationConfiguration;

//-------------------------------------------------------------------	
	public Peer() {
		cyclon = create(Cyclon.class);
		aggregation = create(Aggregation.class);
		bootstrap = create(BootstrapClient.class);

		connect(network, aggregation.getNegative(Network.class));
		connect(network, cyclon.getNegative(Network.class));
		connect(network, bootstrap.getNegative(Network.class));
		connect(timer, aggregation.getNegative(Timer.class));
		connect(timer, cyclon.getNegative(Timer.class));
		connect(timer, bootstrap.getNegative(Timer.class));
		connect(cyclon.getPositive(CyclonPartnersPort.class), aggregation.getNegative(CyclonPartnersPort.class));
		
		subscribe(handleInit, control);
		subscribe(handleJoin, peerPort);
		subscribe(handleJoinCompleted, cyclon.getPositive(CyclonPort.class));
		subscribe(handleBootstrapResponse, bootstrap.getPositive(P2pBootstrap.class));
	}

//-------------------------------------------------------------------	
	Handler<PeerInit> handleInit = new Handler<PeerInit>() {
		public void handle(PeerInit init) {
			num = init.getNum();
			peerSelf = init.getPeerSelf();
			self = peerSelf.getPeerAddress();
			CyclonConfiguration cyclonConfiguration = init.getCyclonConfiguration();
			aggregationConfiguration = init.getApplicationConfiguration();
			
			bootstrapRequestPeerCount = cyclonConfiguration.getBootstrapRequestPeerCount();

			trigger(new CyclonInit(cyclonConfiguration), cyclon.getControl());
			trigger(new BootstrapClientInit(self, init.getBootstrapConfiguration()), bootstrap.getControl());
		}
	};

//-------------------------------------------------------------------	
	Handler<JoinPeer> handleJoin = new Handler<JoinPeer>() {
		public void handle(JoinPeer event) {
			BootstrapRequest request = new BootstrapRequest("Cyclon", bootstrapRequestPeerCount);
			trigger(request, bootstrap.getPositive(P2pBootstrap.class));
			
			Snapshot.addPeer(peerSelf);
		}
	};

//-------------------------------------------------------------------	
	Handler<BootstrapResponse> handleBootstrapResponse = new Handler<BootstrapResponse>() {
		public void handle(BootstrapResponse event) {
			if (!bootstrapped) {
				Set<PeerEntry> somePeers = event.getPeers();
				LinkedList<PeerAddress> cyclonInsiders = new LinkedList<PeerAddress>();
				
				for (PeerEntry peerEntry : somePeers)
					cyclonInsiders.add((PeerAddress) peerEntry.getOverlayAddress());
				
				trigger(new CyclonJoin(peerSelf, cyclonInsiders), cyclon.getPositive(CyclonPort.class));
				bootstrapped = true;
			}
		}
	};

//-------------------------------------------------------------------	
	Handler<JoinCompleted> handleJoinCompleted = new Handler<JoinCompleted>() {
		public void handle(JoinCompleted event) {
			trigger(new BootstrapCompleted("Cyclon", peerSelf), bootstrap.getPositive(P2pBootstrap.class));
			trigger(new AggregationInit(peerSelf, num, aggregationConfiguration), aggregation.getControl());

		}
	};
}
