/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.best;

import se.sics.kompics.PortType;

/**
 *
 * @author julio
 */
public class BestEffortBroadcast extends PortType {
    {
        indication(BebDeliver.class);
        request(BebBroadcast.class);
    }
    
}
