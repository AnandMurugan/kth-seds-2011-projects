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
    private Address node;
    private int period;

    public Restore(Address node, int period) {
        this.node = node;
        this.period = period;
    }

    public Address getNode() {
        return node;
    }

    public int getPeriod() {
        return period;
    }
}
