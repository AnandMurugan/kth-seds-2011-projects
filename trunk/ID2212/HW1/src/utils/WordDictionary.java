/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author julio
 */
public class WordDictionary {
    private static List<String> wordList;
    private static Random rd;

    public WordDictionary() {
        // Open file
        wordList = new ArrayList<String>();
        rd = new Random(System.nanoTime());
        wordList.add("test 1");
        wordList.add("test 2");
        wordList.add("test 3");

    }

    public static synchronized String getWord() {
        if (rd == null) {
            rd = new Random(System.nanoTime());
        }
        if (wordList == null) {
            wordList = new ArrayList<String>();
            wordList.add("test 1");
            wordList.add("test 2");
            wordList.add("test 3");
        }

        return wordList.get(rd.nextInt(wordList.size()));
    }

    public void InitWordList() {
        wordList = new ArrayList<String>();
        wordList.add("test 1");
        wordList.add("test 2");
        wordList.add("test 3");
    }
}
