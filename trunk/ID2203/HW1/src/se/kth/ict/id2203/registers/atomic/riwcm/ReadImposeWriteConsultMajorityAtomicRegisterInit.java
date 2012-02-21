/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.registers.atomic.riwcm;

import java.util.Set;
import se.sics.kompics.Init;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class ReadImposeWriteConsultMajorityAtomicRegisterInit extends Init {
    private Set<Address> all;
    private Set<Address> neighbors;
    private Address self;
    private int registerNumber;

    public ReadImposeWriteConsultMajorityAtomicRegisterInit(Set<Address> all, Set<Address> neighbors, Address self, int registerNumber) {
        this.all = all;
        this.neighbors = neighbors;
        this.self = self;
        this.registerNumber = registerNumber;
    }

    public Set<Address> getAll() {
        return all;
    }

    public Set<Address> getNeighbors() {
        return neighbors;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public Address getSelf() {
        return self;
    }
}
