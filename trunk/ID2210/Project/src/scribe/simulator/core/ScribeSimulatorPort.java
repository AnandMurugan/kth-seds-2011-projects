package scribe.simulator.core;

import common.simulation.PeerFail;
import common.simulation.PeerJoin;
import common.simulation.Publish;
import se.sics.kompics.PortType;
import se.sics.kompics.p2p.experiment.dsl.events.TerminateExperiment;

public class ScribeSimulatorPort extends PortType {{
	positive(PeerJoin.class);
	positive(PeerFail.class);
	positive(Publish.class);
	negative(TerminateExperiment.class);
}}
