/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.best.basic;

import java.io.Serializable;
import se.kth.ict.id2203.broadcast.best.BebDeliver;
import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class BebMessage extends Pp2pDeliver {
    //private final String message;
    private final BebDeliver deliverEvent;

    public BebMessage(Address source, BebDeliver deliverEvent) {
        super(source);
        this.deliverEvent = deliverEvent;
    }

    public final BebDeliver getDeliverEvent() {
        return deliverEvent;
    }

    @Override
    public String toString() {
        return "BebMessage{" + "message=" + deliverEvent.getMessage() + ", source=" + super.getSource() + '}';
    }
}
