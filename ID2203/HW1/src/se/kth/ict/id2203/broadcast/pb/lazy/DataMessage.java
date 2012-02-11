/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.ict.id2203.broadcast.pb.lazy;

import se.kth.ict.id2203.flp2p.Flp2pDeliver;
import se.sics.kompics.address.Address;

/**
 *
 * @author Igor
 */
public class DataMessage extends Flp2pDeliver implements Comparable<DataMessage> {
    private final String message;
    private final int sequenceNumber;

    public DataMessage(Address source, String message, int sequenceNumber) {
        super(source);
        this.message = message;
        this.sequenceNumber = sequenceNumber;
    }

    public final String getMessage() {
        return message;
    }

    public final int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String toString() {
        return "DataMessage{" + "source=" + this.getSource() + ", message=" + message + ", sequenceNumber=" + sequenceNumber + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataMessage other = (DataMessage) obj;
        if ((this.getSource() == null) ? (other.getSource() != null) : !this.getSource().equals(other.getSource())) {
            return false;
        }
        if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message)) {
            return false;
        }
        if (this.sequenceNumber != other.sequenceNumber) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.getSource() != null ? this.getSource().hashCode() : 0);
        hash = 41 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 41 * hash + this.sequenceNumber;
        return hash;
    }

    @Override
    public int compareTo(DataMessage o) {
        return this.getSource().equals(o.getSource())
                ? Integer.compare(sequenceNumber, o.sequenceNumber)
                : 0;
    }
}
