/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic;

import se.kth.ict.id2203.link.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class AckMessage extends Pp2pDeliver {
    private int register;
    private int requestId;

    public AckMessage(Address source, int register, int requestId) {
        super(source);
        this.register = register;
        this.requestId = requestId;
    }

    public int getRegister() {
        return register;
    }

    public int getRequestId() {
        return requestId;
    }
}
