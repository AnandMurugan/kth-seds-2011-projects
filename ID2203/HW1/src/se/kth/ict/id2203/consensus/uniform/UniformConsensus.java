/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.consensus.uniform;

import se.sics.kompics.PortType;

/**
 *
 * @author Igor
 */
public class UniformConsensus extends PortType {
    {
        indication(UcDecide.class);
        request(UcPropose.class);
    }
}
