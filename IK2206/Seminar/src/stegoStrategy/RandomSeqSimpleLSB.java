package stegoStrategy;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import stegoFile.ByteHelper;

import common.IContainerFile;
import common.IReadStegoFile;
import common.IStegoStrategy;
import common.IWriteStegoFile;

public class RandomSeqSimpleLSB implements IStegoStrategy {
	private int nrOfBits;
	private int seed;
	
	public RandomSeqSimpleLSB(int nrOfBits, int seed) {
		this.nrOfBits = nrOfBits;
		this.seed = seed;
	}
	
	private Set<Integer> generateIndexes(int nrOfIndexes, int range) {
		Set<Integer> indexValues = new TreeSet<Integer>();
		
		Random rand = new Random(seed);
		while(indexValues.size() < nrOfIndexes/nrOfBits) {
			indexValues.add(rand.nextInt(range));
		}
		
		return indexValues;
	}

	public void encode(IContainerFile cover, IReadStegoFile stego) {
		System.out.println("encoding...");
		Set<Integer> indexValues = generateIndexes(stego.getSize(), cover.getSize());
		int curIndex = 0;
		Iterator<Integer> it = indexValues.iterator();
		while(it.hasNext()) {
			int nextIndex = it.next();
			while(curIndex < nextIndex) {
				cover.getNextByte();
				curIndex++;
			}
			
			byte coverByte = cover.getNextByte();
			byte stegoByte = stego.getNextBits(nrOfBits);
			coverByte = (byte) (coverByte >> nrOfBits);
			coverByte = (byte) (coverByte << nrOfBits);
			coverByte = (byte) (coverByte | stegoByte);
			cover.setByte(coverByte);
			curIndex++;
		}
		if(stego.hasMoreBits()) {
			System.out.println("Couldn't encode the whole message, container was too small");
		}
	}

	public void decode(IContainerFile cover, IWriteStegoFile stego) {
		System.out.println("decoding...");
		
		Set<Integer> indexValues = generateIndexes(stego.getSize(), cover.getSize());
		int curIndex = 0;
		Iterator<Integer> it = indexValues.iterator();
		while(it.hasNext()) {
			int nextIndex = it.next();
			while(curIndex < nextIndex) {
				cover.getNextByte();
				curIndex++;
			}
		
			byte coverByte = cover.getNextByte();
			short mask = ByteHelper.getMask(nrOfBits-1, nrOfBits);
			byte stegoByte = (byte)(coverByte & mask);
			stego.setNextBits(stegoByte, nrOfBits);
			curIndex++;
		}
		if(stego.hasMoreBits()) {
			System.out.println("Couldn't decode the whole message, container was too small");
		}
	}
}
