/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.uniform;

import se.sics.kompics.Event;

/**
 *
 * @author Igor
 */
public class UcPropose extends Event {
    private int id;
    private Object value;

    public UcPropose(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }
}
