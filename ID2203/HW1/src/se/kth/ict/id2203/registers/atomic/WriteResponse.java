/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic;

import se.sics.kompics.Event;

/**
 *
 * @author Igor
 */
public class WriteResponse extends Event {
    private int register;

    public WriteResponse(int register) {
        this.register = register;
    }

    public int getRegister() {
        return register;
    }
}
