/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MessageType;

/**
 *
 * @author julio
 */
public class HangmanHandler extends Thread {
    Socket clientSocket;
    int maxAttemptNumber;
    int totalScore;
    int leftAttempts;
    String gameWord;
    //char[] currentUserWord;
    String missingLettersWord;

    public HangmanHandler(Socket clientSocket, int maxAttemptNum) {
        this.clientSocket = clientSocket;
        this.maxAttemptNumber = maxAttemptNum;

    }

    @Override
    public void run() {
        //BufferedInputStream in;
        //BufferedOutputStream out;
        //DataInputStream inData;
        //DataOutputStream outData;
        BufferedReader reader = null;
        PrintWriter wr = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String str;

            wr = new PrintWriter(clientSocket.getOutputStream()); // output stream

            //inData = new DataInputStream(clientSocket.getInputStream());
            //outData = new DataOutputStream(clientSocket.getOutputStream());

            while ((str = reader.readLine()) != null) {
                MessageType msgType = MessageType.valueOf(str);
                switch (msgType) {
                    case CLIENT_NEW_GAME:
                        newGame();
                        wr.println(MessageType.SERVER_NEW_GAME);
                        wr.println(gameWord);
                        wr.flush();
                        break;
                    case CLIENT_GUESS_LETTER:
                        if ((str = reader.readLine()) == null) {
                            // TODO. handle error
                            reader.close();
                            return;
                        } else {
                            // TODO. find indexes and return to client, attach score
                        }
                        ;
                        break;
                    case CLIENT_GUESS_WORD:
                        if ((str = reader.readLine()) == null) {
                            // TODO. handle error
                            reader.close();
                            return;
                        } else {
                            if (gameWord.equals(str)) {
                                // TODO. respond client won
                            } else {
                                // TODO. respond client lost
                            }
                        }
                        ;
                        break;
                    case CLIENT_DISCONNECT:
                        reader.close();
                        break;
                }
            }
            reader.close();
            wr.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } finally {
        }

        //int messageType = inData.r();

        /*switch(MessageType.values()[]){
        case MessageType.CLIENT_NEW_GAME:break;
        
        
        }*/

        //if(type == MessageType.CLIENT_NEW_GAME){
        // reset game state fields
        // send SERVER_START NEW GAME}
        //(type == CLIENT_DISCONNECT)
        // close socket, end loop;
        // implement a timer when client is inactive for a long period.
    }

    public void newGame() {
        this.gameWord = "test";
        this.missingLettersWord = "test";
        this.leftAttempts = this.maxAttemptNumber;
    }
}
