package aggregation.system.peer.aggregation;

import common.configuration.AggregationConfiguration;
import common.peer.PeerAddress;
import se.sics.kompics.Init;

public final class AggregationInit extends Init {

	private final PeerAddress peerSelf;
	private final int num;
	private final AggregationConfiguration configuration;

//-------------------------------------------------------------------
	public AggregationInit(PeerAddress peerSelf, int num, AggregationConfiguration configuration) {
		super();
		this.peerSelf = peerSelf;
		this.num = num;
		this.configuration = configuration;
	}

//-------------------------------------------------------------------
	public PeerAddress getSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------
	public int getNum() {
		return this.num;
	}

//-------------------------------------------------------------------
	public AggregationConfiguration getConfiguration() {
		return this.configuration;
	}
}