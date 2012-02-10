/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class RequestMessage extends Flp2pDeliver {
    private final Address broadcastSource;
    private final int sequenceNumber;
    public int ttl;

    public RequestMessage(Address source, Address broadcastSource, int sequenceNumber, int ttl) {
        super(source);
        this.broadcastSource = broadcastSource;
        this.sequenceNumber = sequenceNumber;
        this.ttl = ttl;
    }

    public final Address getBroadcastSource() {
        return broadcastSource;
    }

    public final int getSequenceNumber() {
        return sequenceNumber;
    }
}