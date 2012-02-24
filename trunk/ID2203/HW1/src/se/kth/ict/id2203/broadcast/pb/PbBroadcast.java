/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb;

import se.sics.kompics.Event;

/**
 *
 * @author Igor
 */
public class PbBroadcast extends Event {
    private PbDeliver deliverEvent;

    public PbBroadcast(PbDeliver deliverEvent) {
        this.deliverEvent = deliverEvent;
    }

    public PbDeliver getDeliverEvent() {
        return deliverEvent;
    }
}
