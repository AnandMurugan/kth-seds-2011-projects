package StegoFileImpl;

import common.IReadStegoFile;
import common.IWriteStegoFile;

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
	public byte getNextBits(int nrOfBits) {
		byte b = messageBytes[byteIndex];
		short aux = 0;
		if(nrOfBits <= bitIndex + 1) {
			short mask = getMask(bitIndex, nrOfBits);
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
	
	/*
	 * the bits are numbered from right to left and the startBit is the one mostly to the left
	 *  to get this mask 00011100, the call would be getMask(4, 3)
	 */
	private short getMask(int startBit, int nrOfBits) {
		short b = 0x0001;
		b = (short) (b << (startBit-nrOfBits+1));
		short mask = 0x0000;
		for(int i = 0; i < nrOfBits; i++) {
			mask = (short) (mask | b);
			b = (short) (b << 1);
		}
		return mask;
	}
	
	
	/*public static void main(String args[]) {
		byte[] test = "some".getBytes();
		IReadStegoFile r = new ReadStegoFileImpl(test);
		IWriteStegoFile w = new WriteStegoFileImpl();
		
		for(int i = 0; i < (4*8/2); i++) {
			w.setNextBits(r.getNextBits(2), 2);
		}
		
		System.out.println("Result");
		System.out.println(new String(w.getMessage()));		
	}*/

}
