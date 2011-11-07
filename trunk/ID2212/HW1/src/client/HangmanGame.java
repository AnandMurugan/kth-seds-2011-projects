/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julio
 */
public class HangmanGame {
    //Game variable declaration
    private int wordLength;
    private String word;
    private int attemptsLeft;
    private int totalScore;
    //Network connection variable decalration
    private String host = "localhost";
    private int port = 8080;
    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;
    private final static int LINGER = 100;
    //UI variable declaration
    private HangmanClient ui;

    public HangmanGame(HangmanClient ui) {
        this.ui = ui;
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            socket.setSoLinger(true, LINGER);
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ui.connectionEstablished();
            ui.updateTitle("Hangman --- Connected to " + host + ":" + port);
        } catch (UnknownHostException ex) {
            ui.updateTitle("Hangman");
            ui.reportConnectionError();
            Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ui.updateTitle("Hangman");
            ui.reportConnectionError();
            Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect(String host) {
        this.host = host;
        connect();
    }

    public void connect(String host, int port) {
        this.host = host;
        this.port = port;
        connect();
    }

    public void disconnect() {
    }

    public void newGame() {
    }

    public void guessLetter(char letter) {
    }

    public void guessWord(String word) {
    }
}
