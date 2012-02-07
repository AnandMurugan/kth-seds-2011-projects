/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.epfd;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class Restore extends Event {

    private Address source;

    public Restore(Address source) {
        this.source = source;
    }

    public Address getSource() {
        return source;
    }
}
