/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julio
 */
public class HangmanServer {
    public static void main(String args[]) {
        int port = 8080;
        int maxAttemptNumber = 6;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Starting server...");
        
        ServerSocket servSocket = null;
        try {
            servSocket = new ServerSocket(port);
            while (true) {
                System.out.println("Waiting for incomming clients...");
                Socket clientSocket = servSocket.accept();
                (new HangmanHandler(clientSocket, maxAttemptNumber)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(HangmanServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
