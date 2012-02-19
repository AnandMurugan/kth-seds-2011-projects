/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.beb.basic;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class BasicMessage extends Pp2pDeliver {
    private final String message;

    public BasicMessage(Address source, String message) {
        super(source);
        this.message = message;
    }

    public final String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" + "message=" + message + ", source=" + super.getSource() + '}';
    }
}
