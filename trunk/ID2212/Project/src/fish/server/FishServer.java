/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

/**
 *
 * @author julio
 */
public class FishServer {
    public static final Integer DEFAULT_PORT = 8080;
    private Map<String, MultiValueMap> allClientFiles;

    public FishServer() {
        this.allClientFiles = new HashMap<String, MultiValueMap>();
    }

    public void run() {
        System.out.println("FISH server started.");

        boolean listening = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);

            while (listening) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client accepted.");

                String clientAddr = clientSocket.getInetAddress().getHostAddress();
                MultiValueMap oneClientFiles;
                if (!allClientFiles.containsKey(clientAddr)) {
                    allClientFiles.put(clientAddr, new MultiValueMap());
                }
                Thread clientConnectionThread = new Thread(new ClientConnectionHandler(clientSocket, allClientFiles));
                clientConnectionThread.setDaemon(true);
                clientConnectionThread.start();

                //TODO add here thread that will check liveness of the client
            }

            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        new FishServer().run();
    }
}
