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
    void loadFile(String path);
    byte getNextByte();
    void setByte(byte newByte);
    int getSize();
}
