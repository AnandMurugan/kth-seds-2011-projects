/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tman.system.peer.tman;

import common.peer.PeerAddress;
import common.peer.PeerMessage;
import java.util.List;

/**
 *
 * @author Igor
 */
public class TManUpdateRequest extends PeerMessage {
    private List<PeerAddress> buffer;

    public TManUpdateRequest(List<PeerAddress> buffer, PeerAddress source, PeerAddress destination) {
        super(source, destination);
        this.buffer = buffer;
    }

    public List<PeerAddress> getBuffer() {
        return buffer;
    }
}
