package scribe.system.peer.scribe;

import common.peer.PeerAddress;
import common.peer.PeerMessage;
import java.math.BigInteger;


public class TopicEvent extends PeerMessage {
	private static final long serialVersionUID = 8493601671018888143L;
	private final BigInteger topicId;

//-------------------------------------------------------------------
	public TopicEvent(BigInteger topicId, PeerAddress source, PeerAddress destination) {
		super(source, destination);
		this.topicId = topicId;
	}

//-------------------------------------------------------------------
	public BigInteger getTopicId() {
		return this.topicId;
	}

//-------------------------------------------------------------------
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((topicId == null) ? 0 : topicId.hashCode());
		return result;
	}

//-------------------------------------------------------------------
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopicEvent other = (TopicEvent) obj;
		if (topicId == null) {
			if (other.topicId != null)
				return false;
		} else if (!topicId.equals(other.topicId))
			return false;
		return true;
	}
	
}
