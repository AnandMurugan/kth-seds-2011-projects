/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwc;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author julio
 */
public class RiwcInit extends Init {
    private final Set<Address> neighborSet;
    private final Address self;
    private final int numRegisters;
    
    public RiwcInit(Set<Address> neighborSet, Address self, int nRegisters) {
        this.neighborSet = neighborSet;
        this.self = self;
        this.numRegisters = nRegisters;
    }
    
    public final Set<Address>  getNeighborSet(){
        return neighborSet;
    }
    
    public final Address getSelf(){
        return self;
    }
    
    public final int getNumRegisters(){
        return numRegisters;
    }
}
