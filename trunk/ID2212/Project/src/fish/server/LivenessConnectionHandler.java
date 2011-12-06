/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author julio
 */
public class LivenessConnectionHandler extends Thread {
    private final static int LINGER = 100;
    private String clientIP;
    private boolean isAlive;

    public LivenessConnectionHandler(String clientIP) {
        this.clientIP = clientIP;
        this.isAlive = true;
    }

    @Override
    public void run() {
        Socket clientLivenessSocket = null;
        while (isAlive) {
            try {
                Thread.sleep(FishServer.LIVENESS_CHECK_INTERVAL);
                clientLivenessSocket = new Socket(clientIP, FishServer.DEFAULT_LIVENESS_PORT);
                clientLivenessSocket.close();
                System.out.println("Client " + clientIP + " is alive.");
            } catch (UnknownHostException ex) {
                System.out.println("ERROR: Client is not available.\n");
                isAlive = false;
                System.out.println("ERROR: Client " + clientIP + " is not responding.");
                // TODO. Remove file info entries from server cache and DB
            } catch (IOException ex) {
                System.out.println("ERROR: " + ex.getMessage() + "\n");
                isAlive = false;
                System.out.println("ERROR: Client " + clientIP + " is not responding.");
                // TODO. Remove file info entries from server cache and DB
            } catch (InterruptedException ex) {
                
            }
        }
    }
}
