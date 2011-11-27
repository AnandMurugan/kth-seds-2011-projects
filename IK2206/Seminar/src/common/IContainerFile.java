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
    public void loadFile(String path);
    public void saveFile(String path);
    public byte getNextByte() throws IndexOutOfBoundsException;
    public void setByte(byte newByte);
    public int getSize();
    public boolean hasMoreBytes();
    public void setByte(int index, byte newByte);
    public void getByte(int index) throws IndexOutOfBoundsException;
}
