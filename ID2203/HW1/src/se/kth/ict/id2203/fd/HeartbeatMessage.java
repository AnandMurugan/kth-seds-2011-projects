/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.fd;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class HeartbeatMessage extends Pp2pDeliver {
    /**
     * Instantiates a new Heartbeat message.
     *
     * @param source the source
     */
    public HeartbeatMessage(Address source) {
        super(source);
    }
}
