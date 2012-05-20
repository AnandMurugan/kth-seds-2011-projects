package scribe.simulator.snapshot;

import common.peer.PeerAddress;
import java.math.BigInteger;
import java.util.ArrayList;


public class PeerInfo {
	private ArrayList<BigInteger> topics;
	private PeerAddress succ;
	private PeerAddress pred;
	private ArrayList<PeerAddress> tmanPartners;
	private ArrayList<PeerAddress> cyclonPartners;
	private ArrayList<BigInteger> receivedTopicEvents;

//-------------------------------------------------------------------
	public PeerInfo(ArrayList<BigInteger> topics) {
		this.topics = new ArrayList<BigInteger>(topics);
		this.tmanPartners = new ArrayList<PeerAddress>();
		this.cyclonPartners = new ArrayList<PeerAddress>();
		this.receivedTopicEvents = new ArrayList<BigInteger>();
	}

//-------------------------------------------------------------------
	public void updateSuccPred(PeerAddress succ, PeerAddress pred) {
		this.succ = succ;
		this.pred = pred;
	}

//-------------------------------------------------------------------
	public void updateTManPartners(ArrayList<PeerAddress> partners) {
		this.tmanPartners = partners;
	}

//-------------------------------------------------------------------
	public void updateCyclonPartners(ArrayList<PeerAddress> partners) {
		this.cyclonPartners = partners;
	}

//-------------------------------------------------------------------
	public void updateRecvTopicEvents(BigInteger event) {
		if (!this.receivedTopicEvents.contains(event))
			this.receivedTopicEvents.add(event);
	}

//-------------------------------------------------------------------
	public PeerAddress getSucc() {
		return this.succ;
	}

//-------------------------------------------------------------------
	public PeerAddress getPred() {
		return this.pred;
	}

//-------------------------------------------------------------------
	public ArrayList<BigInteger> getTopics() {
		return this.topics;
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getTManPartners() {
		return this.tmanPartners;
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getCyclonPartners() {
		return this.cyclonPartners;
	}

//-------------------------------------------------------------------
	public ArrayList<BigInteger> getReceivedTopicEvents() {
		return this.receivedTopicEvents;
	}

}
