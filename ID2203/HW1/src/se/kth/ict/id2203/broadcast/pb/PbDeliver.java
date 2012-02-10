/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb;

import java.io.Serializable;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class PbDeliver extends Event implements Serializable {
    private final String message;
    private final Address source;

    public PbDeliver(Address source, String message) {
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
