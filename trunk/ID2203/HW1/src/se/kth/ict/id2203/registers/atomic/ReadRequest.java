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
public class ReadRequest extends Event {
    private int r;

    public ReadRequest(int r) {
        this.r = r;
    }

    public int getRegister() {
        return r;
    }
}
