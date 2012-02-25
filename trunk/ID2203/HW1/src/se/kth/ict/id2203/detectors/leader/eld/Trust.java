/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.leader.eld;

import se.sics.kompics.Event;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class Trust extends Event {
    private Address leader;

    public Trust(Address leader) {
        this.leader = leader;
    }

    public Address getLeader() {
        return leader;
    }
}
