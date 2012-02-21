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
public class ReadResponse extends Event {
    private int register;
    private int value;

    public ReadResponse(int register, int value) {
        this.register = register;
        this.value = value;
    }

    public int getRegister() {
        return register;
    }

    public int getValue() {
        return value;
    }
}
