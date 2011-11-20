/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author julio
 */
public interface IReadStegoFile {
	public boolean hasMoreBits();
    public byte getNextBits(int nrOfBits) throws IndexOutOfBoundsException;
}
