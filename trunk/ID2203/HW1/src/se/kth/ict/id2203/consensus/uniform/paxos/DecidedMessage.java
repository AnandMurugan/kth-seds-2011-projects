/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.uniform.paxos;

import se.kth.ict.id2203.broadcast.beb.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class DecidedMessage extends BebDeliver {
    private int id;
    private Object value;

    public DecidedMessage(Address source, int id, Object value) {
        super(source);
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }
}
