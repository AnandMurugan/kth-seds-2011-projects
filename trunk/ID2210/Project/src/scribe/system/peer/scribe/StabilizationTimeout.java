/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribe.system.peer.scribe;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author julio
 */
public class StabilizationTimeout extends Timeout {

	public StabilizationTimeout(ScheduleTimeout request) {
		super(request);
	}
}
