/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure.epfd;

import se.sics.kompics.PortType;

/**
 *
 * @author Igor
 */
public class EventuallyPerfectFailureDetector extends PortType {

    {
        indication(Suspect.class);
        indication(Restore.class);
    }
}
