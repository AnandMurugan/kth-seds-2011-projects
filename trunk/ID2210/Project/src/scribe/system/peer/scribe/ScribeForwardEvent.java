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
public class ScribeForwardEvent extends TopicEvent {
    private final ScribeMessageType msgType;

    public ScribeForwardEvent(BigInteger topicId, PeerAddress source, PeerAddress destination, ScribeMessageType msgType) {
        super(topicId, source, destination);
        this.msgType = msgType;
    }
}
