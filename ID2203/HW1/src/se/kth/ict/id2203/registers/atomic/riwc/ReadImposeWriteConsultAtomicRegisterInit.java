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
public class ReadImposeWriteConsultAtomicRegisterInit extends Init {
    private Set<Address> neighborSet;
    private Address self;
    private int numRegisters;

    public ReadImposeWriteConsultAtomicRegisterInit(Set<Address> neighborSet, Address self, int nRegisters) {
        this.neighborSet = neighborSet;
        this.self = self;
        this.numRegisters = nRegisters;
    }

    public Set<Address> getNeighborSet() {
        return neighborSet;
    }

    public Address getSelf() {
        return self;
    }

    public int getNumRegisters() {
        return numRegisters;
    }
}
