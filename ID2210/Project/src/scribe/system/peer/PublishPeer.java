package scribe.system.peer;

import java.math.BigInteger;

import se.sics.kompics.Event;

public class PublishPeer extends Event {

	private final BigInteger topicId;

//-------------------------------------------------------------------
	public PublishPeer(BigInteger topicId) {
		this.topicId = topicId;
	}

//-------------------------------------------------------------------
	public BigInteger getTopicId() {
		return this.topicId;
	}
}
