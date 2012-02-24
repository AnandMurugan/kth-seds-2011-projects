/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import se.sics.kompics.address.Address;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;

/**
 *
 * @author Igor
 */
public class PullingTimeout extends Timeout {
    private Address broadcastSource;
    private int sequenceNumber;

    public PullingTimeout(ScheduleTimeout request, Address broadcastSource, int sequenceNumber) {
        super(request);
        this.broadcastSource = broadcastSource;
        this.sequenceNumber = sequenceNumber;
    }

    public Address getBroadcastSource() {
        return broadcastSource;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
