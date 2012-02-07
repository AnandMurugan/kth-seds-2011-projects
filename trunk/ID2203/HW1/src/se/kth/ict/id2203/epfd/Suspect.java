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
public class Suspect extends Event {
    private Address node;

    public Suspect(Address node) {
        this.node = node;
    }

    public Address getNode() {
        return node;
    }
}
