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
public class NAckMessage extends Pp2pDeliver {
    private int id;

    public NAckMessage(Address source, int id) {
        super(source);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
