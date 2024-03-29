/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.MessageType;
import utils.WordDictionary;

/**
 *
 * @author julio
 */
public class HangmanHandler extends Thread {
    private Socket clientSocket;
    private int maxAttemptNumber;
    private int totalScore;
    private int leftAttempts;
    private String gameWord;
    private char[] currentUserWord;
    private boolean isGameStarted = false;
    private WordDictionary wd;

    public HangmanHandler(Socket clientSocket, int maxAttemptNum, WordDictionary wd) {
        this.clientSocket = clientSocket;
        this.maxAttemptNumber = maxAttemptNum;
        this.wd = wd;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter wr = null;
        boolean isConnected = true;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String str;
            wr = new PrintWriter(clientSocket.getOutputStream()); // output stream
            while (isConnected) {
                if ((str = reader.readLine()) == null) {
                    break;
                }
                System.out.println(Thread.currentThread().getId() + " : " + str);
                StringTokenizer tokens = new StringTokenizer(str, "\t");
                String msgTypeString = tokens.nextToken();
                MessageType msgType = MessageType.valueOf(msgTypeString);
                switch (msgType) {
                    case CLIENT_NEW_GAME:
                        newGame();
                        wr.print(MessageType.SERVER_NEW_GAME);
                        wr.print("\t");
                        wr.print(this.gameWord.length());
                        wr.print("\t");
                        wr.println(this.maxAttemptNumber);
                        wr.flush();
                        break;
                    case CLIENT_GUESS_LETTER:
                        if (tokens.hasMoreTokens()) {
                            String suggestedLetter = tokens.nextToken();
                            if (evaluateLetter(suggestedLetter.charAt(0))) {
                                if (!String.copyValueOf(currentUserWord).contains("-")) {
                                    this.totalScore++;
                                    wr.print(MessageType.SERVER_WIN);
                                    wr.print("\t");
                                    wr.print(this.currentUserWord);
                                    wr.print("\t");
                                    wr.println(this.totalScore);
                                    wr.flush();
                                } else {
                                    wr.print(MessageType.SERVER_CORRECT_LETTER);
                                    wr.print("\t");
                                    wr.println(this.currentUserWord);
                                    wr.flush();
                                }
                            } else {
                                this.leftAttempts--;
                                if (this.leftAttempts == 0) {
                                    this.totalScore--;
                                    wr.print(MessageType.SERVER_GAME_OVER);
                                    wr.print("\t");
                                    wr.println(this.totalScore);
                                    wr.flush();
                                } else {
                                    wr.print(MessageType.SERVER_WRONG_LETTER);
                                    wr.print("\t");
                                    wr.println(this.leftAttempts);
                                    wr.flush();
                                }
                            }
                        } else {
                            // TODO. handle error
                        }

                        ;
                        break;
                    case CLIENT_GUESS_WORD:
                        if (tokens.hasMoreTokens()) {
                            String suggestedWord = tokens.nextToken();
                            if (gameWord.equalsIgnoreCase(suggestedWord)) {
                                this.totalScore++;
                                wr.print(MessageType.SERVER_WIN);
                                wr.print("\t");
                                wr.print(this.gameWord.toUpperCase());
                                wr.print("\t");
                                wr.println(this.totalScore);
                                wr.flush();
                            } else {
                                this.totalScore--;
                                wr.print(MessageType.SERVER_GAME_OVER);
                                wr.print("\t");
                                wr.println(this.totalScore);
                                wr.flush();
                            }
                        }
                        ;
                        break;
                    case CLIENT_DISCONNECT:
                        isConnected = false;
                        break;
                    default:
                    // TODO. handle bad message
                }
            }

            reader.close();
            wr.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(HangmanHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("ReplaceAllDot")
    public void newGame() {
        this.gameWord = wd.getWord();
        this.currentUserWord = this.gameWord.replaceAll(".", "-").toCharArray();
        this.leftAttempts = this.maxAttemptNumber;
        this.isGameStarted = true;
    }

    public boolean evaluateLetter(char letter) {
        char[] word = this.gameWord.toCharArray();
        boolean correct = false;
        for (int i = 0; i < word.length; i++) {
            if (Character.toUpperCase(word[i]) == Character.toUpperCase(letter)) {
                correct = true;
                this.currentUserWord[i] = letter;
            }
        }

        return correct;
    }
}
