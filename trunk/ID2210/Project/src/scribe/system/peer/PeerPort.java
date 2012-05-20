package scribe.system.peer;

import common.peer.JoinPeer;
import se.sics.kompics.PortType;

public class PeerPort extends PortType {{
	negative(JoinPeer.class);
	negative(PublishPeer.class);
}}
