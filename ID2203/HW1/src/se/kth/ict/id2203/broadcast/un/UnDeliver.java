/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un;

import java.io.Serializable;
import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public abstract class UnDeliver extends Event implements Serializable {
    private Address source;

    public UnDeliver(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
}
