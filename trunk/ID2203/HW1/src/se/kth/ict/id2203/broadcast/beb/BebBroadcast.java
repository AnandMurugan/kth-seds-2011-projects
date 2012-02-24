/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.beb;

import se.sics.kompics.Event;

/**
 *
 * @author julio
 */
public class BebBroadcast extends Event {
    private BebDeliver deliverEvent;

    public BebBroadcast(BebDeliver deliverEvent) {
        this.deliverEvent = deliverEvent;
    }

    public BebDeliver getDeliverEvent() {
        return deliverEvent;
    }
}
