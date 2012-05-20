package aggregation.system.peer.aggregation;

import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

public class AggregationSchedule extends Timeout {

	public AggregationSchedule(SchedulePeriodicTimeout request) {
		super(request);
	}

//-------------------------------------------------------------------
	public AggregationSchedule(ScheduleTimeout request) {
		super(request);
	}
}
