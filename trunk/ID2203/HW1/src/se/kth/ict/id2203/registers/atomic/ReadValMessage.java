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
public class ReadValMessage extends Pp2pDeliver {
    private int register;
    private int requestId;
    private int timestamp;
    private int rank;
    private int value;

    public ReadValMessage(Address source, int register, int requestId, int timestamp, int rank, int value) {
        super(source);
        this.register = register;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.rank = rank;
        this.value = value;
    }

    public int getRegister() {
        return register;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getRank() {
        return rank;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getValue() {
        return value;
    }
}
