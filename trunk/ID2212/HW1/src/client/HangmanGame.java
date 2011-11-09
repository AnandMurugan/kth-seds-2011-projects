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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MessageType;

/**
 *
 * @author julio
 */
public class HangmanGame {
    //Game variable declaration
    public final static int MAX_ATTEMPTS = 6;
    private int wordLength;
    private String wordView;
    private int attemptsLeft;
    private int totalScore;
    private boolean playing = false;
    //Network connection variable decalration
    private final static String DEFAULT_HOST = "localhost";
    private final static int DEFAULT_PORT = 8080;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private final static int LINGER = 100;
    //UI variable declaration
    private HangmanClient ui;
    
    public HangmanGame(HangmanClient ui) {
        this.ui = ui;
    }
    
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            socket.setSoLinger(true, LINGER);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            ui.connectionEstablished();
            ui.updateTitle("Hangman --- Connected to " + host + ":" + port);
            ui.updateTotalScore(0);
        } catch (UnknownHostException ex) {
            ui.updateTitle("Hangman");
            ui.reportConnectionError();
            //Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ui.updateTitle("Hangman");
            ui.reportConnectionError();
            //Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect(String host) {
        connect(host, DEFAULT_PORT);
    }
    
    public void connect() {
        connect(DEFAULT_HOST, DEFAULT_PORT);
    }
    
    public void disconnect() {
        if (socket != null) {
            try {
                //TODO: send DISCONNECT message
                
                in.close();
                out.close();
                socket.close();
                
                ui.updateTitle("Hangman");
                ui.connectionClosed();
            } catch (IOException ex) {
                Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void newGame() {
        if (socket != null) {
            try {
                out.write(MessageType.CLIENT_NEW_GAME.name());
                out.newLine();
                out.flush();
                
                String response;
                if ((response = in.readLine()) != null) {
                    String[] msgParts = response.split("\t");
                    MessageType type = MessageType.valueOf(msgParts[0]);
                    switch (type) {
                        case SERVER_NEW_GAME:
                            wordLength = Integer.parseInt(msgParts[1]);
                            
                            char[] dashes = new char[wordLength];
                            Arrays.fill(dashes, '-');
                            wordView = new String(dashes);
                            
                            attemptsLeft = Integer.parseInt(msgParts[2]);
                            
                            playing = true;

                            //Update UI
                            ui.updateWordView(wordView);
                            ui.updateAttemptsLeftCount(attemptsLeft);
                            int stage = 0;
                            ui.updateHangmanImage(stage);
                            
                            break;
                        default:
                        //TODO: server responded with wrong message
                    }
                } else {
                    //TODO: Connection lost
                }
            } catch (IOException ex) {
                Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void guessLetter(char letter) {
        if ((socket != null) && playing) {
            try {
                out.write(MessageType.CLIENT_GUESS_LETTER.name() + "\t" + letter);
                out.newLine();
                out.flush();
                
                String response;
                if ((response = in.readLine()) != null) {
                    String[] msgParts = response.split("\t");
                    MessageType type = MessageType.valueOf(msgParts[0]);
                    switch (type) {
                        case SERVER_CORRECT_LETTER:
                            wordView = msgParts[1];

                            //Update UI
                            ui.updateWordView(wordView);
                            
                            break;
                        case SERVER_WRONG_LETTER:
                            attemptsLeft = Integer.parseInt(msgParts[1]);

                            //Update UI
                            ui.updateAttemptsLeftCount(attemptsLeft);
                            int stage = MAX_ATTEMPTS - attemptsLeft;
                            ui.updateHangmanImage(stage);
                            
                            break;
                        case SERVER_WIN:
                            wordView = msgParts[1];
                            totalScore = Integer.parseInt(msgParts[2]);
                            
                            playing = false;

                            //Update UI
                            ui.updateWordView(wordView);
                            ui.reportWin();
                            ui.updateTotalScore(totalScore);
                            
                            break;
                        case SERVER_GAME_OVER:
                            totalScore = Integer.parseInt(msgParts[1]);
                            
                            playing = false;

                            //Update UI
                            ui.reportGameOver();
                            ui.updateTotalScore(totalScore);
                            
                            break;
                        default:
                        //TODO: server responded with wrong message
                    }
                } else {
                    //TODO: Connection lost
                }
            } catch (IOException ex) {
                Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void guessWord(String word) {
        if ((socket != null) && playing) {
            try {
                out.write(MessageType.CLIENT_GUESS_WORD.name() + "\t" + word);
                out.newLine();
                out.flush();
                
                String response;
                if ((response = in.readLine()) != null) {
                    String[] msgParts = response.split("\t");
                    MessageType type = MessageType.valueOf(msgParts[0]);
                    switch (type) {
                        case SERVER_WIN:
                            wordView = msgParts[1];
                            totalScore = Integer.parseInt(msgParts[2]);
                            
                            playing = false;

                            //Update UI
                            ui.updateWordView(word);
                            ui.reportWin();
                            ui.updateTotalScore(totalScore);
                            
                            break;
                        case SERVER_GAME_OVER:
                            totalScore = Integer.parseInt(msgParts[1]);
                            
                            playing = false;

                            //Update UI
                            ui.reportGameOver();
                            ui.updateTotalScore(totalScore);
                            
                            break;
                        default:
                        //TODO: server responded with wrong message
                    }
                } else {
                    //TODO: Connection lost
                }
            } catch (IOException ex) {
                Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
