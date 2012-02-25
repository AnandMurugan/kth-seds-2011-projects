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
public class WriteAckMessage extends Pp2pDeliver {
    private int id;
    private int sentTimestamp;

    public WriteAckMessage(Address source, int id, int sentTimestamp) {
        super(source);
        this.id = id;
        this.sentTimestamp = sentTimestamp;
    }

    public int getId() {
        return id;
    }

    public int getSentTimestamp() {
        return sentTimestamp;
    }
}
