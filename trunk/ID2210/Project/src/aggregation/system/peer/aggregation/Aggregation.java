package aggregation.system.peer.aggregation;

import aggregation.simulator.snapshot.Snapshot;
import common.configuration.AggregationConfiguration;
import common.peer.PeerAddress;
import java.util.ArrayList;

import cyclon.system.peer.cyclon.CyclonPartnersPort;
import cyclon.system.peer.cyclon.CyclonPartnersRequest;
import cyclon.system.peer.cyclon.CyclonPartnersResponse;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.Timer;


public final class Aggregation extends ComponentDefinition {

	Positive<CyclonPartnersPort> partnersPort = positive(CyclonPartnersPort.class);
	Positive<Network> networkPort = positive(Network.class);
	Positive<Timer> timerPort = positive(Timer.class);

	private PeerAddress self;
	private long period;
	private double num;
	private ArrayList<PeerAddress> cyclonPartners;
	private AggregationConfiguration aggregationConfiguration;

//-------------------------------------------------------------------	
	public Aggregation() {
		cyclonPartners = new ArrayList<PeerAddress>();
		
		subscribe(handleInit, control);
		subscribe(handleRequestPartners, timerPort);
		subscribe(handleRecvPartners, partnersPort);
	}

//-------------------------------------------------------------------	
	Handler<AggregationInit> handleInit = new Handler<AggregationInit>() {
		public void handle(AggregationInit init) {
			self = init.getSelf();
			num = init.getNum();
			aggregationConfiguration = init.getConfiguration();
			period = aggregationConfiguration.getPeriod();

			SchedulePeriodicTimeout rst = new SchedulePeriodicTimeout(period, period);
			rst.setTimeoutEvent(new AggregationSchedule(rst));
			trigger(rst, timerPort);

			Snapshot.updateNum(self, num);
		}
	};

//-------------------------------------------------------------------	
	Handler<AggregationSchedule> handleRequestPartners = new Handler<AggregationSchedule>() {
		public void handle(AggregationSchedule event) {
			CyclonPartnersRequest request = new CyclonPartnersRequest();
			trigger(request, partnersPort);
			
			Snapshot.updateNum(self, num);
		}
	};

//-------------------------------------------------------------------	
	Handler<CyclonPartnersResponse> handleRecvPartners = new Handler<CyclonPartnersResponse>() {
		public void handle(CyclonPartnersResponse event) {
			cyclonPartners = event.getPartners();
		}
	};
}
