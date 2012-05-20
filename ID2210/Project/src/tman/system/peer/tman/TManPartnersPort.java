package tman.system.peer.tman;

import se.sics.kompics.PortType;

public final class TManPartnersPort extends PortType {{
	negative(TManPartnersRequest.class);
	positive(TManPartnersResponse.class);
}}
