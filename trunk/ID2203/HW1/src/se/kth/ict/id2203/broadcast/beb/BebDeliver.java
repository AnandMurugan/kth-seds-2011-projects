/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.beb;

import java.io.Serializable;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public abstract class BebDeliver extends Event implements Serializable {
    private final Address source;

    public BebDeliver(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
}
