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
public abstract class PbDeliver extends Event implements Serializable {
    private Address source;

    public PbDeliver(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
}
