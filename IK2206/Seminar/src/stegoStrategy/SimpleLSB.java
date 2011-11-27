package stegoStrategy;

import stegoFile.ByteHelper;
import stegoFile.WriteStegoFileImpl;
import common.IContainerFile;
import common.IReadStegoFile;
import common.IStegoStrategy;
import common.IWriteStegoFile;

public class SimpleLSB implements IStegoStrategy {
	int nrOfBits;
	
	public SimpleLSB(int nrOfBits) {
		this.nrOfBits = nrOfBits;
	}
	
	public void encode(IContainerFile cover, IReadStegoFile stego) {
		System.out.println("encoding...");
		while(cover.hasMoreBytes() && stego.hasMoreBits()) {
			byte coverByte = cover.getNextByte();
			byte stegoByte = stego.getNextBits(nrOfBits);
			coverByte = (byte) (coverByte >> nrOfBits);
			coverByte = (byte) (coverByte << nrOfBits);
			coverByte = (byte) (coverByte | stegoByte);
			cover.setByte(coverByte);
		}
		if(stego.hasMoreBits()) {
			System.out.println("Couldn't encode the whole message, container was too small");
		}
	}

	public void decode(IContainerFile cover, IWriteStegoFile stego) {
		System.out.println("decoding...");
		
		while(cover.hasMoreBytes() && stego.hasMoreBits()) {
			byte coverByte = cover.getNextByte();
			short mask = ByteHelper.getMask(nrOfBits-1, nrOfBits);
			byte stegoByte = (byte)(coverByte & mask);
			stego.setNextBits(stegoByte, nrOfBits);
		}
		if(stego.hasMoreBits()) {
			System.out.println("Couldn't decode the whole message, container was too small");
		}
	}

}
