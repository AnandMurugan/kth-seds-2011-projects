/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import java.io.File;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author Igor
 */
class PeerRequestHandler extends Thread {
    public PeerRequestHandler(Socket clientSocket) {
    }

    PeerRequestHandler(Socket clientSocket, Map<String, File> sharedFiles) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
