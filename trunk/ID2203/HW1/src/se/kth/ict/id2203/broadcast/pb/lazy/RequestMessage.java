/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import se.kth.ict.id2203.links.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class RequestMessage extends Flp2pDeliver {
    private Address broadcastSource;
    private int sequenceNumber;
    public int ttl;

    public RequestMessage(Address source, Address broadcastSource, int sequenceNumber, int ttl) {
        super(source);
        this.broadcastSource = broadcastSource;
        this.sequenceNumber = sequenceNumber;
        this.ttl = ttl;
    }

    public Address getBroadcastSource() {
        return broadcastSource;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String toString() {
        return "RequestMessage{" + "source=" + super.getSource() + ",broadcastSource=" + broadcastSource + ", sequenceNumber=" + sequenceNumber + ", ttl=" + ttl + '}';
    }
}
