package tman.system.peer;

import common.configuration.CyclonConfiguration;
import common.configuration.TManConfiguration;
import common.peer.PeerAddress;
import se.sics.kompics.Init;
import se.sics.kompics.p2p.bootstrap.BootstrapConfiguration;

public final class PeerInit extends Init {

	private final PeerAddress peerSelf;
	private final BootstrapConfiguration bootstrapConfiguration;
	private final CyclonConfiguration cyclonConfiguration;
	private final TManConfiguration tmanConfiguration;

//-------------------------------------------------------------------	
	public PeerInit(PeerAddress peerSelf, BootstrapConfiguration bootstrapConfiguration, CyclonConfiguration cyclonConfiguration, TManConfiguration tmanConfiguration) {
		super();
		this.peerSelf = peerSelf;
		this.bootstrapConfiguration = bootstrapConfiguration;
		this.cyclonConfiguration = cyclonConfiguration;
		this.tmanConfiguration = tmanConfiguration;
	}

//-------------------------------------------------------------------	
	public PeerAddress getPeerSelf() {
		return this.peerSelf;
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
	public TManConfiguration getTManConfiguration() {
		return this.tmanConfiguration;
	}

}
