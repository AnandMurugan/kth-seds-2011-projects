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
    private int port = 8080;
    private int maxAttemptNumber = 6;
    public void main(String args[]) {
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        ServerSocket servSocket = null;
        try {
            servSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = servSocket.accept();
                (new HangmanHandler(clientSocket, maxAttemptNumber)).start();
            }

        } catch (IOException ex) {
            Logger.getLogger(HangmanServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
