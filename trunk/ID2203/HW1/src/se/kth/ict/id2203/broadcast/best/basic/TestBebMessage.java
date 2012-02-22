/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.best.basic;

import se.kth.ict.id2203.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class TestBebMessage extends Pp2pDeliver {
    private final String msg;
    
    public TestBebMessage(Address source, String msg) {
        super(source);
        this.msg = msg;
    }
    
    public String getMessage(){
        return msg;
    }
}
