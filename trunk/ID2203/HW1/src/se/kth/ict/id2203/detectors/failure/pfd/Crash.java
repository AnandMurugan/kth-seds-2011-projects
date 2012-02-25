/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure.pfd;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class Crash extends Event {
    private Address nodeCrashed;

    public Crash(Address nodeCrashed) {
        this.nodeCrashed = nodeCrashed;
    }

    public Address getNodeCrashed() {
        return nodeCrashed;
    }
}
