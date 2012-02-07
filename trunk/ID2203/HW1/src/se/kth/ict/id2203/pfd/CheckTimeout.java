/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfd;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author julio
 */
public final class CheckTimeout extends Timeout {
    /**
     * Instantiates a new application continue.
     * 
     * @param request
     *            the request
     */
    public CheckTimeout(ScheduleTimeout request) {
        super(request);
    }
}
