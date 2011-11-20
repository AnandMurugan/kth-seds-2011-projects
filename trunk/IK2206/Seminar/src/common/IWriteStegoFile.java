package common;

public interface IWriteStegoFile {
	 public void setNextBits(byte writeByte, int nrOfBits) throws IndexOutOfBoundsException;
	 public byte[] getMessage();
	 public boolean hasMoreBits();
}
