/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un.simple;

import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class SimpleMessage extends Flp2pDeliver {
    private final String message;

    public SimpleMessage(Address source, String message) {
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
