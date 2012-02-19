/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.beb;

import se.sics.kompics.Event;

/**
 *
 * @author julio
 */
public class BebBroadcast extends Event {
    private final String message;

    public BebBroadcast(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
}
