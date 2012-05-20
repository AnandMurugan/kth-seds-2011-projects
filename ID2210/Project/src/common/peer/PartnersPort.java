package common.peer;

import se.sics.kompics.PortType;

public final class PartnersPort extends PortType {{
	negative(PartnersRequest.class);
	positive(PartnersResponse.class);
}}
