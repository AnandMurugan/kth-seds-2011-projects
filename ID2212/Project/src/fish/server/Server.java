/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author julio
 */
public class Server {
    private static final Integer DEFAULT_PORT = 8080;

    public void main(String args[]) throws IOException {
        boolean listening = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port:" + DEFAULT_PORT + ".");
            System.exit(1);
        }

        while (listening) {
            //Socket clientSocket = serverSocket.accept();
            //(new SimpleConnectionHandler(clientSocket)).start();
        }

        serverSocket.close();
    }
}
