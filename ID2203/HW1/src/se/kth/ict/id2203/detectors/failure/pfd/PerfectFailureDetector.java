/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.detectors.failure.pfd;

import se.sics.kompics.PortType;

/**
 *
 * @author julio
 */
public class PerfectFailureDetector extends PortType {
    {
        indication(Crash.class);
    }
}
