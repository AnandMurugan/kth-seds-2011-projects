/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic;

import se.sics.kompics.PortType;

/**
 *
 * @author Igor
 */
public class AtomicRegister extends PortType {
    {
        indication(ReadResponse.class);
        indication(WriteResponse.class);
        request(ReadRequest.class);
        request(WriteRequest.class);
    }
}
