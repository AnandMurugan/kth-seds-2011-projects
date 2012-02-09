/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.fd.pfd;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public final class Crash extends Event {
    private final Address nodeCrashed;

    public Crash(Address nodeCrashed) {
        this.nodeCrashed = nodeCrashed;
    }

    public Address getNodeCrashed() {
        return nodeCrashed;
    }
}
