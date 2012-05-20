package common.peer;

import java.util.ArrayList;

import se.sics.kompics.Event;


public class PartnersResponse extends Event {
	ArrayList<PeerAddress> partners = new ArrayList<PeerAddress>();

//-------------------------------------------------------------------
	public PartnersResponse(ArrayList<PeerAddress> partners) {
		this.partners = partners;
	}
        
	public PartnersResponse() {
	}

//-------------------------------------------------------------------
	public ArrayList<PeerAddress> getPartners() {
		return this.partners;
	}
}
