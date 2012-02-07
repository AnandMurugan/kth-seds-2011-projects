/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.pfd;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public final class HeartbeatMessage extends Pp2pDeliver {
    /**
     * 
     */
    private static final long serialVersionUID = -1077397194946341231L;
    private final int id;

    /**
     * Instantiates a new Heartbeat message.
     * 
     * @param source
     *            the source
     * @param id
     *            the id of the heartbeat
     */
    protected HeartbeatMessage(Address source, int id) {
        super(source);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
