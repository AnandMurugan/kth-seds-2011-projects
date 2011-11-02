/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author julio
 */
public enum MessageType {
    CLIENT_NEW_GAME,
    CLIENT_GUESS_LETTER,
    CLIENT_GUESS_WORD,
    SERVER_NEW_GAME,
    SERVER_WRONG_LETTER,
    SERVER_CORRECT_LETTER,
    SERVER_GAME_OVER,
    SERVER_WIN
}
