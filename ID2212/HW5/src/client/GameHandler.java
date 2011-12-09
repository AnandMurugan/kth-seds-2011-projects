/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.TextBox;

/**
 *
 * @author julio
 */
public class GameHandler {
    public final static int MAX_ATTEMPTS = 6;
    private int wordLength;
    private String wordView;
    private int attemptsLeft;
    private int totalScore;
    private boolean playing = false;
    private StreamConnection sc;
    private OutputStream out;
    private InputStream in;
    private Display display;
    private TextBox tf;
    private HangmanClient ui;

    public GameHandler(StreamConnection sc, TextBox tf, HangmanClient ui) {
        this.sc = sc;
        this.tf = tf;
        this.display = display;
        this.ui = ui;
    }

    public void run() {
        try {
            InputStream is = null;
            is = sc.openInputStream();
            int ch = 0;
            while ((ch = is.read()) != -1) {
                tf.setString(tf.getString() + (char) ch);
            }
            is.close();
        } catch (Exception e) {
        }
    }

    public void connect(String host, int port) {

        try {
            sc = (StreamConnection) Connector.open("socket://"
                    + host + ":"+ port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            out = sc.openDataOutputStream();
            in = sc.openDataInputStream();
            tf.setString("Connected");
            ui.displayHangmanForm();
        } catch (IOException ex) {
            //ui.updateTitle("Hangman");
            //ui.reportConnectionError();
            //Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
        }
    }

    public void newGame() {
        if (sc != null) {
            try {
                out.write(MessageType.CLIENT_NEW_GAME.getBytes(), 0, MessageType.CLIENT_NEW_GAME.length());
                out.write('\n');
                out.flush();

                String response;
                StringBuffer str = new StringBuffer();
                int ch;
                while ((ch = in.read()) != '\n') {
                    str.append((char) ch);
                }
                response = str.toString();
                System.out.println(str);
                String messageType = response.substring(0, MessageType.SERVER_NEW_GAME.length());

                if (messageType.equals(MessageType.SERVER_NEW_GAME)) {
                    int tabIndex = response.indexOf('\t', MessageType.SERVER_NEW_GAME.length() + 1);
                    String wordLengthText = response.substring(MessageType.SERVER_NEW_GAME.length() + 1, tabIndex);
                    wordLength = Integer.parseInt(wordLengthText);

                    char[] dashes = new char[wordLength];

                    for (int i = 0; i < dashes.length; i++) {
                        dashes[i] = '-';
                    }

                    wordView = new String(dashes);
                    String attemptsLeftStr = response.substring(tabIndex + 1, response.length() - 1);
                    attemptsLeft = Integer.parseInt(attemptsLeftStr);
                    playing = true;
                    // Update UI
                    ui.displayNewGame(wordView, attemptsLeftStr);
                } else {
                    return;
                }

            } catch (IOException ex) {
                //ui.updateTitle("Hangman");
                //ui.reportConnectionError();
                System.out.println(ex);
            }
        }
    }

    public void guessLetter(char letter) {
        if ((sc != null) && playing) {
            try {
                String messageToServer = MessageType.CLIENT_GUESS_LETTER + '\t' + letter;
                out.write(messageToServer.getBytes(), 0, messageToServer.length());
                out.write('\n');
                out.flush();

                String response;
                StringBuffer str = new StringBuffer();
                int ch;
                while ((ch = in.read()) != '\n') {
                    str.append((char) ch);
                }
                response = str.toString();

                System.out.println(response);
                int tabIndexMsgType = response.indexOf('\t');
                String messageType = response.substring(0, tabIndexMsgType);

                if (messageType.equals(MessageType.SERVER_CORRECT_LETTER)) {
                    wordView = response.substring(tabIndexMsgType + 1, response.length() - 1);
                    ui.updateGameStatusForm(wordView, String.valueOf(attemptsLeft), true);

                } else if (messageType.equals(MessageType.SERVER_WRONG_LETTER)) {
                    attemptsLeft = Integer.parseInt(response.substring(tabIndexMsgType + 1, response.length() - 1));
                    ui.updateGameStatusForm(wordView, String.valueOf(attemptsLeft), true);
                } else if (messageType.equals(MessageType.SERVER_WIN)) {
                    int tabIndexWordView = response.indexOf('\t', tabIndexMsgType + 1);
                    wordView = response.substring(tabIndexMsgType + 1, tabIndexWordView);
                    String totalScoreStr = response.substring(tabIndexWordView + 1, response.length() - 1);
                    totalScore = Integer.parseInt(totalScoreStr);
                    playing = false;
                    ui.displayWinGame(wordView, totalScoreStr);
                } else if (messageType.equals(MessageType.SERVER_GAME_OVER)) {
                    String totalScoreStr = response.substring(tabIndexMsgType + 1, response.length() - 1);
                    totalScore = Integer.parseInt(totalScoreStr);
                    playing = false;
                    ui.displayLostGame(totalScoreStr);
                } else {
                    tf.setString("ERROR");
                }

            } catch (IOException ex) {
                playing = false;
                sc = null;
                //ui.updateTitle("Hangman");
                //ui.reportConnectionError();
                //Logger.getLogger(HangmanGame.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }
        }
    }

    public void guessWord(String word) {
        if ((sc != null) && playing) {
            try {
                String messageToServer = MessageType.CLIENT_GUESS_WORD + '\t' + word;
                out.write(messageToServer.getBytes(), 0, messageToServer.length());
                out.write('\n');
                out.flush();

                String response;
                StringBuffer str = new StringBuffer();
                int ch;
                while ((ch = in.read()) != '\n') {
                    str.append((char) ch);
                }
                response = str.toString();

                System.out.println(response);
                int tabIndexMsgType = response.indexOf('\t');
                String messageType = response.substring(0, tabIndexMsgType);

                if (messageType.equals(MessageType.SERVER_WIN)) {
                    int tabIndexWordView = response.indexOf('\t', tabIndexMsgType + 1);
                    wordView = response.substring(tabIndexMsgType + 1, tabIndexWordView);
                    String totalScoreStr = response.substring(tabIndexWordView + 1, response.length() - 1);
                    totalScore = Integer.parseInt(totalScoreStr);
                    playing = false;
                    ui.displayWinGame(wordView, totalScoreStr);
                } else if (messageType.equals(MessageType.SERVER_GAME_OVER)) {
                    String totalScoreStr = response.substring(tabIndexMsgType + 1, response.length() - 1);
                    totalScore = Integer.parseInt(totalScoreStr);
                    playing = false;
                    ui.displayLostGame(totalScoreStr);
                } else {
                    // TODO. some handling
                }

            } catch (IOException ex) {
                playing = false;
                sc = null;
                System.out.println(ex);
            }
        }
    }
    
    public void close() {
        try {
            sc.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}