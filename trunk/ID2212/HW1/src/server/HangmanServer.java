/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.HangmanGame;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.WordDictionary;

/**
 *
 * @author julio
 */
public class HangmanServer {
    private final static int DEFAULT_PORT = HangmanGame.DEFAULT_PORT;

    public static void main(String args[]) {
        int port = DEFAULT_PORT;
        WordDictionary wd = new WordDictionary();

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
                System.out.println("New client connection started.");
                (new HangmanHandler(clientSocket, HangmanGame.MAX_ATTEMPTS, wd)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(HangmanServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
