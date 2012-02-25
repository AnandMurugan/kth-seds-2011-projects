/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.abortable;

import se.kth.ict.id2203.links.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class ReadAckMessage extends Pp2pDeliver {
    private int id;
    private int timestamp;
    private Object value;
    private int sentTimestamp;

    public ReadAckMessage(Address source, int id, int timestamp, Object value, int sentTimestamp) {
        super(source);
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
        this.sentTimestamp = sentTimestamp;
    }

    public int getId() {
        return id;
    }

    public int getSentTimestamp() {
        return sentTimestamp;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Object getValue() {
        return value;
    }
}
