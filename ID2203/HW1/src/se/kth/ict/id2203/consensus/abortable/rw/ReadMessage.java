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
public class ReadMessage extends BebDeliver {
    private int id;
    private int timestamp;

    public ReadMessage(Address source, int id, int timestamp) {
        super(source);
        this.id = id;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
