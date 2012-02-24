/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import se.kth.ict.id2203.broadcast.pb.PbDeliver;
import se.kth.ict.id2203.broadcast.un.UnDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class LazyProbabilisticBroadcastMessage extends UnDeliver {
    private PbDeliver deliverEvent;
    private int sequenceNumber;

    public LazyProbabilisticBroadcastMessage(Address source, PbDeliver deliverEvent, int sequenceNumber) {
        super(source);
        this.deliverEvent = deliverEvent;
        this.sequenceNumber = sequenceNumber;
    }

    public PbDeliver getDeliverEvent() {
        return deliverEvent;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
