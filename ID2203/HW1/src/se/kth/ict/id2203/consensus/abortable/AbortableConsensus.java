/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.abortable;

import se.sics.kompics.PortType;

/**
 *
 * @author Igor
 */
public class AbortableConsensus extends PortType {
    {
        indication(AcDecide.class);
        request(AcPropose.class);
    }
}
