/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwc;

import se.kth.ict.id2203.broadcast.best.BebDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class WriteMessage extends BebDeliver {
    final int r;
    final int v;
    final int id;
    final int timestamp;
    final int rank;

    public WriteMessage(Address a, 
            String msg, 
            int r, 
            int v,
            int id,
            int timestamp,
            int rank) {
        super(a, msg);
        this.r = r;
        this.v = v;
        this.id = id;
        this.timestamp = timestamp;
        this.rank = rank;
    }
    
    public int getR(){
        return r;
    }
    
    public int getV(){
        return v;
    }
    
    public int getId(){
        return id;
    }
    
    public int getTimestamp(){
        return timestamp;
    }
    
    public int getRank(){
        return rank;
    }
} 