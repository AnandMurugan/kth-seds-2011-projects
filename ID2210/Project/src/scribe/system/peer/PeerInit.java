package scribe.system.peer;

import common.configuration.CyclonConfiguration;
import common.configuration.TManConfiguration;
import common.peer.PeerAddress;
import java.math.BigInteger;
import java.util.ArrayList;

import se.sics.kompics.Init;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;

public final class PeerInit extends Init {

	private final PeerAddress peerSelf;
	private final ArrayList<BigInteger> topics;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;
	private final TManConfiguration tmanConfiguration;

//-------------------------------------------------------------------	
	public PeerInit(PeerAddress peerSelf, ArrayList<BigInteger> topics, BootstrapConfiguration bootstrapConfiguration, CyclonConfiguration cyclonConfiguration, TManConfiguration tmanConfiguration) {
		super();
		this.peerSelf = peerSelf;
		this.topics = topics;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
		this.tmanConfiguration = tmanConfiguration;
	}

//-------------------------------------------------------------------	
	public PeerAddress getPeerSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------	
	public ArrayList<BigInteger> getTopics() {
		return this.topics;
	}

//-------------------------------------------------------------------	
	public BootstrapConfiguration getBootstrapConfiguration() {
		return this.bootstrapConfiguration;
	}

//-------------------------------------------------------------------	
	public CyclonConfiguration getCyclonConfiguration() {
		return this.cyclonConfiguration;
	}

//-------------------------------------------------------------------	
	public TManConfiguration getApplicationConfiguration() {
		return this.tmanConfiguration;
	}

}
