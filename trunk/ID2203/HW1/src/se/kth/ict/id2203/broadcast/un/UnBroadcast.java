/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un;

import se.sics.kompics.Event;

/**
 *
 * @author Igor
 */
public class UnBroadcast extends Event {
    private UnDeliver deliverEvent;

    public UnBroadcast(UnDeliver deliverEvent) {
        this.deliverEvent = deliverEvent;
    }

    public UnDeliver getDeliverEvent() {
        return deliverEvent;
    }
}
