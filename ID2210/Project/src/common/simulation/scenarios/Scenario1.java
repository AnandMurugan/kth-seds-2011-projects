package common.simulation.scenarios;

import common.simulation.scenarios.Operations;
import se.sics.kompics.p2p.experiment.dsl.SimulationScenario;

@SuppressWarnings("serial")
public class Scenario1 extends Scenario {
	private static SimulationScenario scenario = new SimulationScenario() {{
		StochasticProcess process1 = new StochasticProcess() {{
			eventInterArrivalTime(constant(100));
			raise(10, Operations.peerJoin(110), uniform(13));
		}};
		
		StochasticProcess process2 = new StochasticProcess() {{
			eventInterArrivalTime(constant(100));
			raise(90, Operations.peerJoin(10), uniform(13));
		}};

		process1.start();
		process2.startAfterTerminationOf(2000, process1);
	}};
	
//-------------------------------------------------------------------
	public Scenario1() {
		super(scenario);
	} 
}
