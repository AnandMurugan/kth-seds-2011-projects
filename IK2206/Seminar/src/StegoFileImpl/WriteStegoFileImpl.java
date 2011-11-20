package StegoFileImpl;

import common.IWriteStegoFile;

public class WriteStegoFileImpl implements IWriteStegoFile{
	private byte[] messageBytes;
	private int byteIndex;
	private int bitIndex;
	private int maxByteIndex;
	
	public WriteStegoFileImpl() {
		byteIndex = 0;
		bitIndex = 7;
		maxByteIndex = 20;
		messageBytes = new byte[20];
	}

	public void setNextBits(byte writeByte, int nrOfBits) {
		System.out.println(byteIndex + " " + bitIndex + " " + messageBytes[byteIndex]);
		if( nrOfBits <= bitIndex + 1) {
			writeByte = (byte) (writeByte << (bitIndex - nrOfBits + 1));
			byte b = messageBytes[byteIndex];
			System.out.println(b + " " + writeByte);
			b = (byte) (b | writeByte);
			messageBytes[byteIndex] = b;
			bitIndex = bitIndex - nrOfBits;
			if(bitIndex < 0) {
				if(byteIndex == maxByteIndex - 1) {
					increaseMessageSize();
				}
				byteIndex = byteIndex + 1;
				bitIndex = 7;
			}
		}
	}
	
	private void increaseMessageSize() {
		maxByteIndex = maxByteIndex + 20;
		byte[] aux = messageBytes;
		messageBytes = new byte[maxByteIndex];
		System.arraycopy(aux, 0, messageBytes, 0, aux.length);
	}

	public byte[] getMessage() {
		return messageBytes;
	}
}
