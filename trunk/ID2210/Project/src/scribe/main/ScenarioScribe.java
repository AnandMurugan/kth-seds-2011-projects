package scribe.main;

import common.simulation.scenarios.*;
import common.simulation.scenarios.Operations;
import se.sics.kompics.p2p.experiment.dsl.SimulationScenario;

@SuppressWarnings("serial")
public class ScenarioScribe extends Scenario {
	private static SimulationScenario scenario = new SimulationScenario() {{
		StochasticProcess process1 = new StochasticProcess() {{
			eventInterArrivalTime(constant(100));
			raise(1, Operations.peerJoin(5), uniform(13));
		}};
		
		StochasticProcess process2 = new StochasticProcess() {{
			eventInterArrivalTime(constant(100));
			raise(29, Operations.peerJoin(5), uniform(13));
		}};

		StochasticProcess process3 = new StochasticProcess() {{
			eventInterArrivalTime(constant(1));
			raise(1, Operations.publish);
		}};

		process1.start();
		process2.startAfterTerminationOf(2000, process1);
		process3.startAfterTerminationOf(2000, process2);
	}};
	
//-------------------------------------------------------------------
	public ScenarioScribe() {
		super(scenario);
	} 
}
