/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import common.IContainerFile;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.labbookpages.WavFile;
import uk.co.labbookpages.WavFileException;

/**
 *
 * @author Igor
 */
public class WavFileContainer implements IContainerFile {
    private WavFile wavFile;
    private byte[] bytes;
    private byte[] swappedBytes;
    private int currentIndex;
    private int bytesPerSample;
    private long numFrames;
    private int numChannels;
    private int validBits;
    private long sampleRate;

    public WavFileContainer() {
    }

    @Override
    public void loadFile(String path) {
        try {
            wavFile = WavFile.openWavFile(new File(path));
            numChannels = wavFile.getNumChannels();
            numFrames = wavFile.getNumFrames();
            validBits = wavFile.getValidBits();
            sampleRate = wavFile.getSampleRate();
            bytesPerSample = validBits / 8;

            int length = (int) (numFrames * numChannels * bytesPerSample);
            bytes = new byte[length];
            swappedBytes = new byte[length];
            long[] buff = new long[(int) numFrames * numChannels];
            wavFile.readFrames(buff, (int) (numFrames /*/ numChannels*/));
            wavFile.close();
            //System.out.println(Arrays.toString(buff));

            for (int i = 0; i < numFrames * numChannels; i++) {
                for (int j = 0; j < bytesPerSample; j++) {
                    bytes[i * bytesPerSample + j] = (byte) (buff[i] & 0xFF);
                    swappedBytes[i * bytesPerSample + j] = swapEndianness(bytes[i * bytesPerSample + j]);
                    buff[i] >>= 8;
                }
            }

            currentIndex = -bytesPerSample;
        } catch (IOException ex) {
            Logger.getLogger(WavFileContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WavFileException ex) {
            Logger.getLogger(WavFileContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveFile(String path) {
        try {
            long[] buff = new long[(int) numFrames * numChannels];
            for (int i = 0; i < numFrames * numChannels; i++) {
                for (int j = 0; j < bytesPerSample; j++) {
                    buff[i] |= 0xFF & bytes[i * bytesPerSample + (bytesPerSample - j - 1)];
                    buff[i] <<= 8;
                }
                buff[i] >>= 8;
            }
            //System.out.println(Arrays.toString(buff));

            wavFile = WavFile.newWavFile(new File(path), numChannels, numFrames, validBits, sampleRate);
            wavFile.writeFrames(buff, (int) (numFrames /*/ numChannels*/));
            wavFile.close();
        } catch (IOException ex) {
            Logger.getLogger(WavFileContainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WavFileException ex) {
            Logger.getLogger(WavFileContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte getNextByte() throws IndexOutOfBoundsException {
        currentIndex += bytesPerSample;
        return swappedBytes[currentIndex];
    }

    @Override
    public void setByte(byte newByte) {
        bytes[currentIndex] = swapEndianness(newByte);
    }

    @Override
    public int getSize() {
        return (int) numFrames * numChannels;
    }

    @Override
    public boolean hasMoreBytes() {
        return currentIndex < numFrames * numChannels;
    }

    @Override
    public void setByte(int index, byte newByte) {
        bytes[index*bytesPerSample] = swapEndianness(newByte);
    }

    @Override
    public byte getByte(int index) throws IndexOutOfBoundsException {
        return swappedBytes[index*bytesPerSample];
    }

    private byte swapEndianness(byte val) {
        byte swapVal = 0;
        byte mask = 1;
        for (int i = 0; i < 8; i++) {
            swapVal |= mask & val;
            if (i < 7) {
                swapVal <<= 1;
                val >>= 1;
            }
        }
        return swapVal;
    }
}
