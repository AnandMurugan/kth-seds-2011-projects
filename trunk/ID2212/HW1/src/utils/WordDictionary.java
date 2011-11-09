/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julio
 */
public class WordDictionary {
    private final static String DICTIONARY_PATH = "data\\dict.txt";
    private static List<String> wordList;
    private static Random rnd;

    public WordDictionary() {
        this(DICTIONARY_PATH);
    }

    public WordDictionary(String dictionaryPath) {
        // Open file
        rnd = new Random(System.nanoTime());
        wordList = new ArrayList<String>();
        try {
            BufferedReader file = new BufferedReader(new FileReader(DICTIONARY_PATH));

            String word;
            while ((word = file.readLine()) != null) {
                wordList.add(word);
            }
            file.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WordDictionary.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WordDictionary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized String getWord() {
        return wordList.get(rnd.nextInt(wordList.size()));
    }
}
