/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.un;

import se.sics.kompics.Event;

/**
 *
 * @author Igor
 */
public class UnBroadcast extends Event {
    private final String message;

    public UnBroadcast(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
