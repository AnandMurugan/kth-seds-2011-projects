/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.peer;

import java.io.Serializable;

/**
 * Simple structure for storing network address of the peer
 * @author Igor
 */
public class PeerAddress implements Serializable {
    private String host;
    private int port;

    /**
     * Creates a new {@code PeerAddress}
     * @param host Peer host
     * @param port Peer port
     */
    public PeerAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Gets peer host
     * @return host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets peer port
     * @return port
     */
    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PeerAddress other = (PeerAddress) obj;
        if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
            return false;
        }
        if (this.port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 13 * hash + this.port;
        return hash;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}