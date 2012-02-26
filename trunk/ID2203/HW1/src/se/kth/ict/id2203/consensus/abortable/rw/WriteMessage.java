/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.abortable.rw;

import se.kth.ict.id2203.broadcast.beb.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class WriteMessage extends BebDeliver {
    private int id;
    private int timestamp;
    private Object value;

    public WriteMessage(Address source, int id, int timestamp, Object value) {
        super(source);
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Object getValue() {
        return value;
    }
}
