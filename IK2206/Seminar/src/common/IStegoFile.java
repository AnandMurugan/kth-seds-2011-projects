/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author julio
 */
public interface IStegoFile {
    public byte getNextBits(int nrOfBits);
    public void setNextBits(byte b, int nrOfBits);
}
