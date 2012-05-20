package scribe.system.peer.scribe;

import common.peer.PeerAddress;
import java.math.BigInteger;
import java.util.ArrayList;

import se.sics.kompics.Init;

public final class ScribeInit extends Init {

	private final PeerAddress peerSelf;
	private final ArrayList<BigInteger> topics;
	private final long period;

//-------------------------------------------------------------------
	public ScribeInit(PeerAddress peerSelf, ArrayList<BigInteger> topics, long period) {
		super();
		this.peerSelf = peerSelf;
		this.topics = topics;
		this.period = period;
	}

//-------------------------------------------------------------------
	public PeerAddress getSelf() {
		return this.peerSelf;
	}

//-------------------------------------------------------------------
	public ArrayList<BigInteger> getTopics() {
		return this.topics;
	}

//-------------------------------------------------------------------
	public long getPeriod() {
		return this.period;
	}
}