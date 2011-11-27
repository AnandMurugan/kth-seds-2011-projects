package stegoStrategy;

import java.util.Iterator;
import java.util.Set;

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

    public void encode(IContainerFile cover, IReadStegoFile stego) {
        System.out.println("encoding...");
        RandomUnique randGen = new RandomUnique(seed, cover.getSize());
        Set<Integer> indexValues = randGen.nextInt(stego.getSize() / nrOfBits);
        Iterator<Integer> it = indexValues.iterator();
        while (it.hasNext()) {
            int nextIndex = it.next();

            byte coverByte = cover.getByte(nextIndex);
            byte stegoByte = stego.getNextBits(nrOfBits);
            coverByte = (byte) (coverByte >> nrOfBits);
            coverByte = (byte) (coverByte << nrOfBits);
            coverByte = (byte) (coverByte | stegoByte);
            cover.setByte(nextIndex, coverByte);
        }
        if (stego.hasMoreBits()) {
            System.out.println("Couldn't encode the whole message, container was too small");
        }
    }

    public void decode(IContainerFile cover, IWriteStegoFile stego) {
        System.out.println("decoding...");

        RandomUnique randGen = new RandomUnique(seed, cover.getSize());
        Set<Integer> indexValues = randGen.nextInt(stego.getSize() / nrOfBits);
        Iterator<Integer> it = indexValues.iterator();
        while (it.hasNext()) {
            int nextIndex = it.next();

            byte coverByte = cover.getByte(nextIndex);
            short mask = ByteHelper.getMask(nrOfBits - 1, nrOfBits);
            byte stegoByte = (byte) (coverByte & mask);
            stego.setNextBits(stegoByte, nrOfBits);
        }
        if (stego.hasMoreBits()) {
            System.out.println("Couldn't decode the whole message, container was too small");
        }
    }
}
