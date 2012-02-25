/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author julio
 */
public class HeartbeatTimeout extends Timeout {
    /**
     * Instantiates a new application continue.
     *
     * @param request the request
     */
    public HeartbeatTimeout(ScheduleTimeout request) {
        super(request);
    }
}
