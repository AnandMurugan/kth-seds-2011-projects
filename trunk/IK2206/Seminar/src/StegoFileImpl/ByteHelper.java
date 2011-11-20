package StegoFileImpl;

public class ByteHelper {
	
	/*
	 * the bits are numbered from right to left and the startBit is the one mostly to the left
	 *  to get this mask 00011100, the call would be getMask(4, 3)
	 */
	public static short getMask(int startBit, int nrOfBits) {
		short b = 0x0001;
		b = (short) (b << (startBit-nrOfBits+1));
		short mask = 0x0000;
		for(int i = 0; i < nrOfBits; i++) {
			mask = (short) (mask | b);
			b = (short) (b << 1);
		}
		return mask;
	}
}
