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
public class CheckTimeout extends Timeout {
    public CheckTimeout(ScheduleTimeout request) {
        super(request);
    }
}
