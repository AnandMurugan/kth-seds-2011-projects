/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.fd;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author julio
 */
public final class HeartbeatTimeout extends Timeout {
    /**
     * Instantiates a new application continue.
     * 
     * @param request
     *            the request
     */
    public HeartbeatTimeout(ScheduleTimeout request) {
        super(request);
    }
}
