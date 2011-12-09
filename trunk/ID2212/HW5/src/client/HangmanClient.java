/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.OutputStream;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;

/**
 * @author julio
 */
public class HangmanClient extends MIDlet implements CommandListener {
    private final int DEFAULT_PORT = 8085;
    Form connectForm;
    Form guessLetterForm;
    Form guessWordForm;
    Form gameStatusForm;
    TextBox outputForm;
    TextField port;
    TextField serverIP;
    TextField inputText;
    TextField inputWordText;
    // Game status components
    StringItem currentWord;
    StringItem attemptsLeft;
    StringItem score;
    boolean playing = false;
    private Command connectCommand, sendCommand, sendWordCommand, displayStatusCommand,
            playCommand, newGameCommand, guessCommand, guessWordCommand;
    private Command exitCommand, exitCommand2;
    private Display display;
    private Alert incorrectGuessAlert;
    private Alert correctGuessAlert;
    private Alert noGameAlert;
    private Alert winGameAlert;
    private Alert looseGameAlert;
    StreamConnection sc;
    OutputStream os;
    GameHandler game;

    public HangmanClient() {
        super();
        display = Display.getDisplay(this);
        connectForm = new Form("Connect");
        guessLetterForm = new Form("Guess Letter");
        guessWordForm = new Form("Guess Word");
        outputForm = new TextBox("Current Hangman Text:", "", 1024,
                TextField.ANY);
        gameStatusForm = new Form("Hangman-game");
        serverIP = new TextField("Server IP", "127.0.0.1", 50, 0);
        port = new TextField("Port", "8085", 50, 0);
        inputText = new TextField("Input letter", "", 1, 0);
        inputWordText = new TextField("Input word", "", 30, 0);
        //Game status form components
        currentWord = new StringItem("Game word: ", "");
        attemptsLeft = new StringItem("Attempts left: ", "");
        score = new StringItem("Score: ", "0");
        // Commands
        connectCommand = new Command("Connect", Command.SCREEN, 1);
        exitCommand = new Command("Exit", Command.SCREEN, 2);
        sendCommand = new Command("Send", Command.SCREEN, 1);
        sendWordCommand = new Command("Send", Command.SCREEN, 1);
        displayStatusCommand = new Command("Game status", Command.SCREEN, 2);
        newGameCommand = new Command("New game", Command.SCREEN, 2);
        exitCommand2 = new Command("Exit", Command.SCREEN, 2);
        playCommand = new Command("Play", Command.SCREEN, 1);
        guessCommand = new Command("Guess", Command.SCREEN, 1);
        guessWordCommand = new Command("Guess Word", Command.SCREEN, 2);
        incorrectGuessAlert = new Alert("Info", "Incorrect letter", null, AlertType.INFO);
        correctGuessAlert = new Alert("Info", "Good! Correct letter!", null, AlertType.INFO);
        noGameAlert = new Alert("Info", "You need to start a new game", null, AlertType.INFO);
        noGameAlert.setTimeout(Alert.FOREVER);
        winGameAlert = new Alert("Info", "Congratulations, you won!", null, AlertType.INFO);
        winGameAlert.setTimeout(Alert.FOREVER);
        looseGameAlert = new Alert("Info", "Sorry, you lost!", null, AlertType.INFO);
        looseGameAlert.setTimeout(Alert.FOREVER);

        game = new GameHandler(sc, outputForm, this);
    }

    public void startApp() {
        connectForm.addCommand(connectCommand);
        connectForm.addCommand(exitCommand);
        connectForm.append(serverIP);
        connectForm.append(port);
        connectForm.setCommandListener(this);
        // Guess letter form
        guessLetterForm.addCommand(sendCommand);
        guessLetterForm.addCommand(displayStatusCommand);
        guessLetterForm.addCommand(guessWordCommand);
        guessLetterForm.addCommand(exitCommand2);
        guessLetterForm.append(inputText);
        guessLetterForm.setCommandListener(this);
        // Guess Word form
        guessWordForm.addCommand(sendWordCommand);
        guessWordForm.addCommand(displayStatusCommand);
        guessWordForm.addCommand(exitCommand2);
        guessWordForm.append(inputWordText);
        guessWordForm.setCommandListener(this);
        // Status form
        gameStatusForm.append(currentWord);
        gameStatusForm.append(attemptsLeft);
        gameStatusForm.append(score);
        gameStatusForm.addCommand(newGameCommand);
        gameStatusForm.addCommand(guessCommand);
        gameStatusForm.addCommand(exitCommand2);
        gameStatusForm.setCommandListener(this);
        // Result form
        outputForm.addCommand(playCommand);
        outputForm.addCommand(exitCommand);
        outputForm.setCommandListener(this);
        display.setCurrent(connectForm);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }

    public void commandAction(Command c, Displayable d) {
        try {
            if (c == exitCommand) {
                destroyApp(false);
                notifyDestroyed();
            } else if (c == exitCommand2) {
                game.close();
                destroyApp(false);
                notifyDestroyed();
            } else if (c == connectCommand) {
                Runnable connectTask = new Runnable() {
                    public void run() {
                        if (!port.getString().equals("")) {
                        game.connect(serverIP.getString(), Integer.parseInt(port.getString()));
                        } else {
                            game.connect(serverIP.getString(), DEFAULT_PORT);
                        }
                    }
                };
                (new Thread(connectTask)).start();
            } else if (c == displayStatusCommand) {
                if (playing) {
                    display.setCurrent(gameStatusForm);
                } else {
                    display.setCurrent(outputForm);
                }
            } else if (c == sendCommand) {
                if (inputText.getString().length() > 0 && playing) {
                    Runnable sendGuessLetterTask = new Runnable() {
                        public void run() {
                            game.guessLetter(inputText.getString().charAt(0));
                        }
                    };
                    (new Thread(sendGuessLetterTask)).start();
                }
            } else if (c == sendWordCommand) {
                if (inputWordText.getString().length() > 0 && playing) {
                    Runnable sendGuessWordTask = new Runnable() {
                        public void run() {
                            game.guessWord(inputWordText.getString());
                        }
                    };
                    (new Thread(sendGuessWordTask)).start();
                }
            } else if (c == newGameCommand) {
                Runnable newGameTask = new Runnable() {
                    public void run() {
                        game.newGame();
                    }
                };
                (new Thread(newGameTask)).start();
            } else if (c == guessCommand) {
                if (playing) {
                    display.setCurrent(guessLetterForm);
                } else {
                    display.setCurrent(noGameAlert, gameStatusForm);
                }
            } else if (c == guessWordCommand) {
                display.setCurrent(guessWordForm);
            }
        } catch (Exception e) {
            outputForm.insert("error", 0);
            outputForm.insert(e.toString(), 0);
            display.setCurrent(outputForm);
        }
    }

    public void displayHangmanForm() {
        display.setCurrent(gameStatusForm);
    }

    public void displayNewGame(String currentWord, String attemptsLeft) {
        this.currentWord.setText(currentWord);
        this.attemptsLeft.setText(attemptsLeft);
        playing = true;
        display.setCurrent(gameStatusForm);
    }

    public void updateGameStatusForm(String currentWord, String attemptsLeft, boolean playing) {
        this.currentWord.setText(currentWord);
        boolean correctGuess = true;
        if (!this.attemptsLeft.getText().equals(attemptsLeft)) {
            this.attemptsLeft.setText(attemptsLeft);
            correctGuess = false;
        }

        this.playing = playing;
        if (correctGuess) {
            display.setCurrent(correctGuessAlert, gameStatusForm);
        } else {
            display.setCurrent(incorrectGuessAlert, gameStatusForm);
        }
    }

    public void displayWinGame(String word, String score) {
        this.currentWord.setText(word);
        this.score.setText(score);
        this.playing = false;
        display.setCurrent(winGameAlert, gameStatusForm);
    }

    public void displayLostGame(String score) {
        this.score.setText(score);
        this.playing = false;
        display.setCurrent(looseGameAlert, gameStatusForm);
    }
}
