package common;

public interface IWriteStegoFile {
	 public void setNextBits(byte writeByte, int nrOfBits);
	 public byte[] getMessage();
}
