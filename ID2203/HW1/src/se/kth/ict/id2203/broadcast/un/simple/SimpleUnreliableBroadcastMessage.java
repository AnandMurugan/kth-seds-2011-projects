/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un.simple;

import se.kth.ict.id2203.broadcast.un.UnDeliver;
import se.kth.ict.id2203.links.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class SimpleUnreliableBroadcastMessage extends Flp2pDeliver {
    private UnDeliver deliverEvent;

    public SimpleUnreliableBroadcastMessage(Address source, UnDeliver deliverEvent) {
        super(source);
        this.deliverEvent = deliverEvent;
    }

    public UnDeliver getDeliverEvent() {
        return deliverEvent;
    }
}
