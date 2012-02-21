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
public class WriteRequest extends Event {
    private int r;
    private int val;

    public WriteRequest(int r, int val) {
        this.r = r;
        this.val = val;
    }

    public int getRegister() {
        return r;
    }

    public int getValue() {
        return val;
    }
}
