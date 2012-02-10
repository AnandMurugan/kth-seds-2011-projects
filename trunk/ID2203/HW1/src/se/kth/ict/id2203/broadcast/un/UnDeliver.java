/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class UnDeliver extends Event {
    private final String message;
    private final Address source;

    public UnDeliver(Address source, String message) {
        this.message = message;
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public Address getSource() {
        return source;
    }
}
