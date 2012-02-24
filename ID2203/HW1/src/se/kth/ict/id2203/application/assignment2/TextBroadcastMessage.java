/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.application.assignment2;

import se.kth.ict.id2203.broadcast.pb.PbDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class TextBroadcastMessage extends PbDeliver {
    private String text;

    public TextBroadcastMessage(Address source, String text) {
        super(source);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
