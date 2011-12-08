/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.peer;

import java.io.Serializable;

/**
 *
 * @author Igor
 */
public class PeerAddress implements Serializable {
    private String host;
    private int port;

    public PeerAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

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