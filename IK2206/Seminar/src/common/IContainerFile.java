/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author julio
 */
public interface IContainerFile {
    void loadFile();
    byte getNextByte();
    void setByte();
    int getSize();
}
