/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.abortable.rw;

import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class ReadWriteAbortableConsensusInit extends Init {
    private Address self;
    private int n;

    public ReadWriteAbortableConsensusInit(Address self, int n) {
        this.self = self;
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public Address getSelf() {
        return self;
    }
}
