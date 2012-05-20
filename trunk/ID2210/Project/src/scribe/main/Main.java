package scribe.main;

import common.configuration.Configuration;
import common.simulation.scenarios.Scenario;
import scribe.simulator.core.ScribeSimulationMain;

public class Main {
	public static void main(String[] args) throws Throwable {
		Configuration configuration = new Configuration();
		configuration.set();
		
		Scenario scenario = new ScenarioScribe();
		scenario.setSeed(System.currentTimeMillis());
        scenario.getScenario().simulate(ScribeSimulationMain.class);
	}
}
