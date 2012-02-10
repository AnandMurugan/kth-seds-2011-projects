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
    private final Address broadcastSource;
    private final int sequenceNumber;

    public PullingTimeout(ScheduleTimeout request, Address broadcastSource, int sequenceNumber) {
        super(request);
        this.broadcastSource = broadcastSource;
        this.sequenceNumber = sequenceNumber;
    }

    public final Address getBroadcastSource() {
        return broadcastSource;
    }

    public final int getSequenceNumber() {
        return sequenceNumber;
    }
}
