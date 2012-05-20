package tman.system.peer.tman;

import se.sics.kompics.timer.SchedulePeriodicTimeout;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

public class TManSchedule extends Timeout {

	public TManSchedule(SchedulePeriodicTimeout request) {
		super(request);
	}

//-------------------------------------------------------------------
	public TManSchedule(ScheduleTimeout request) {
		super(request);
	}
}
