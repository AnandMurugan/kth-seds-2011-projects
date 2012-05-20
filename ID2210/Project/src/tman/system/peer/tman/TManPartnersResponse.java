package tman.system.peer.tman;

import java.util.ArrayList;

import common.peer.PeerAddress;

import se.sics.kompics.Event;


public class TManPartnersResponse extends Event {
	ArrayList<PeerAddress> partners = new ArrayList<PeerAddress>();

//-------------------------------------------------------------------
	public TManPartnersResponse(ArrayList<PeerAddress> partners) {
		this.partners = partners;
	}
        
	public TManPartnersResponse() {
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getPartners() {
		return this.partners;
	}
}
