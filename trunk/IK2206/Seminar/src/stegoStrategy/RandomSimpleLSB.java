package stegoStrategy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import stegoFile.ByteHelper;

import common.IContainerFile;
import common.IReadStegoFile;
import common.IStegoStrategy;
import common.IWriteStegoFile;

public class RandomSimpleLSB implements IStegoStrategy {
	private int nrOfBits;
	private int seed;
	
	public RandomSimpleLSB(int nrOfBits, int seed) {
		this.nrOfBits = nrOfBits;
		this.seed = seed;
	}

	public void encode(IContainerFile cover, IReadStegoFile stego) {
		System.out.println("encoding...");
		RandomUnique randGen = new RandomUnique(seed, cover.getSize());
		while(stego.hasMoreBits()) {
			int nextIndex = randGen.nextInt();
			
			byte coverByte = cover.getByte(nextIndex);
			byte stegoByte = stego.getNextBits(nrOfBits);
			coverByte = (byte) (coverByte >> nrOfBits);
			coverByte = (byte) (coverByte << nrOfBits);
			coverByte = (byte) (coverByte | stegoByte);
			cover.setByte(nextIndex, coverByte);
		}
		if(stego.hasMoreBits()) {
			System.out.println("Couldn't encode the whole message, container was too small");
		}
	}

	public void decode(IContainerFile cover, IWriteStegoFile stego) {
		System.out.println("decoding...");
		
		RandomUnique randGen = new RandomUnique(seed, cover.getSize());
		while(stego.hasMoreBits()) {
			int nextIndex = randGen.nextInt();
		
			byte coverByte = cover.getByte(nextIndex);
			short mask = ByteHelper.getMask(nrOfBits-1, nrOfBits);
			byte stegoByte = (byte)(coverByte & mask);
			stego.setNextBits(stegoByte, nrOfBits);
		}
		if(stego.hasMoreBits()) {
			System.out.println("Couldn't decode the whole message, container was too small");
		}
	}
}
