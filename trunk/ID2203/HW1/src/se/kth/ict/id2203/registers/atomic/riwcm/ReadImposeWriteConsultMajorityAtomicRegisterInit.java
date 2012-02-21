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
    private Address self;
    private int registerNumber;

    public ReadImposeWriteConsultMajorityAtomicRegisterInit(Set<Address> all, Address self, int registerNumber) {
        this.all = all;
        this.self = self;
        this.registerNumber = registerNumber;
    }

    public Set<Address> getAll() {
        return all;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public Address getSelf() {
        return self;
    }
}
