/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julio
 */
public class HangmanHandler extends Thread {
    Socket clientSocket;
    int maxAttemptNumber;

    public HangmanHandler(Socket clientSocket, int maxAttemptNum) {
        this.clientSocket = clientSocket;
        this.maxAttemptNumber = maxAttemptNum;
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String str;
            while (true) {
                
                
                //if(type == MessageType.CLIENT_NEW_GAME){
                // reset game state fields
                // send SERVER_START NEW GAME}
                //(type == CLIENT_DISCONNECT)
                // close socket, end loop;
                // implement a timer when client is inactive for a long period.
                
            }

        } catch (IOException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
