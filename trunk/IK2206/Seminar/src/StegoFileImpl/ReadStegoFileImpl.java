package StegoFileImpl;

import common.IReadStegoFile;

public class ReadStegoFileImpl implements IReadStegoFile {
	private byte[] messageBytes;
	private int byteIndex;
	private int bitIndex;
	
	public ReadStegoFileImpl(byte[] messageBytes) {
		this.messageBytes = messageBytes;
		byteIndex = 0;
		bitIndex = 7;
	}
	
	
	/*
	 * works for chunks of bits of 1,2,4,8 
	 * for chuncks of 3,5,6,7 i need to take parts of different bytes when i return 
	 */
	public byte getNextBits(int nrOfBits) throws IndexOutOfBoundsException{
		if(hasMoreBits() == false) {
			throw new IndexOutOfBoundsException();
		}
		byte b = messageBytes[byteIndex];
		short aux = 0;
		if(nrOfBits <= bitIndex + 1) {
			short mask = ByteHelper.getMask(bitIndex, nrOfBits);
			aux = (short) (b & mask);
			byte returnByte = (byte) (aux >> (bitIndex - nrOfBits + 1));
			bitIndex = bitIndex - nrOfBits;
			if(bitIndex < 0) {
				byteIndex++;
				bitIndex = 7;
			}
			return returnByte;
		}
		return 0;
	}

	public boolean hasMoreBits() {
		if(byteIndex >= messageBytes.length) {
			return false;
		}
		return true;
	}
}
