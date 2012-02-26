/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.beb.basic;

import se.kth.ict.id2203.broadcast.beb.BebDeliver;
import se.kth.ict.id2203.link.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class BasicBroadcastMessage extends Pp2pDeliver {
    private BebDeliver deliverEvent;

    public BasicBroadcastMessage(Address source, BebDeliver deliverEvent) {
        super(source);
        this.deliverEvent = deliverEvent;
    }

    public BebDeliver getDeliverEvent() {
        return deliverEvent;
    }
}
