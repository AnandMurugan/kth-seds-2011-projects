/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.leader.eld;

import se.sics.kompics.PortType;

/**
 *
 * @author Igor
 */
public class EventualLeaderDetector extends PortType {
    {
        indication(Trust.class);
    }
}
