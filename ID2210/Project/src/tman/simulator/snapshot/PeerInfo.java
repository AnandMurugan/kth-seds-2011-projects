package tman.simulator.snapshot;

import common.peer.PeerAddress;
import java.util.ArrayList;


public class PeerInfo {
	private PeerAddress succ;
	private PeerAddress pred;
	private ArrayList<PeerAddress> tmanPartners;
	private ArrayList<PeerAddress> cyclonPartners;

//-------------------------------------------------------------------
	public PeerInfo() {
		this.tmanPartners = new ArrayList<PeerAddress>();
		this.cyclonPartners = new ArrayList<PeerAddress>();
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
	public PeerAddress getSucc() {
		return this.succ;
	}

//-------------------------------------------------------------------
	public PeerAddress getPred() {
		return this.pred;
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getTManPartners() {
		return this.tmanPartners;
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getCyclonPartners() {
		return this.cyclonPartners;
	}
}
