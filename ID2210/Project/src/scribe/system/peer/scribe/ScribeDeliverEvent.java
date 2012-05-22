/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scribe.system.peer.scribe;

import common.peer.PeerAddress;
import java.math.BigInteger;

/**
 *
 * @author julio
 */
public class ScribeDeliverEvent extends TopicEvent {
    private static final long serialVersionUID = 8493601671018888143L;
    ScribeMessageType msgType;
    
    public ScribeDeliverEvent(BigInteger topicId, PeerAddress source, PeerAddress destination, ScribeMessageType msgType){
        super(topicId, source, destination);
        this.msgType = msgType;
    }
}
