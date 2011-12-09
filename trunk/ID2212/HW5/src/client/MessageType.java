/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author julio
 */
public class MessageType {
    public final static String CLIENT_NEW_GAME = "CLIENT_NEW_GAME";
    public final static String CLIENT_GUESS_LETTER = "CLIENT_GUESS_LETTER";
    public final static String CLIENT_GUESS_WORD = "CLIENT_GUESS_WORD";
    public final static String CLIENT_DISCONNECT = "CLIENT_DISCONNECT";
    public final static String SERVER_NEW_GAME = "SERVER_NEW_GAME";
    public final static String SERVER_WRONG_LETTER = "SERVER_WRONG_LETTER";
    public final static String SERVER_CORRECT_LETTER = "SERVER_CORRECT_LETTER";
    public final static String SERVER_GAME_OVER = "SERVER_GAME_OVER";
    public final static String SERVER_WIN = "SERVER_WIN";
}
