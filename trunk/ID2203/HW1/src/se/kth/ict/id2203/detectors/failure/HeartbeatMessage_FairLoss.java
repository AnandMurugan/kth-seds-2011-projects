/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure;

import se.kth.ict.id2203.link.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class HeartbeatMessage_FairLoss extends Flp2pDeliver {
    /**
     * Instantiates a new Heartbeat message.
     *
     * @param source the source
     */
    public HeartbeatMessage_FairLoss(Address source) {
        super(source);
    }
}
