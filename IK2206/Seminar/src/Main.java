
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import stegoFile.ReadStegoFileImpl;
import stegoFile.WriteStegoFileImpl;
import stegoStrategy.RandomSeqSimpleLSB;
import stegoStrategy.SimpleLSB;
import common.IContainerFile;
import common.IReadStegoFile;
import common.IStegoStrategy;
import common.IWriteStegoFile;
import container.BmpFileContainer;
import container.WavFileContainer;
import stegoStrategy.RandomSimpleLSB;

public class Main {
    public static void main(String args[]) {
        Main m = new Main();

        try {
            //m.test1();
            m.test2();
            System.in.read();
            m.test3();
            System.in.read();
            m.test4();
            System.in.read();
            m.test5();
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void test1() {
        System.out.println("test1");
        byte[] test = "some one is going out tonight and will not work tomorow".getBytes();
        IReadStegoFile r = new ReadStegoFileImpl(test);
        IWriteStegoFile w = new WriteStegoFileImpl();

        while (r.hasMoreBits()) {
            w.setNextBits(r.getNextBits(2), 2);
        }

        System.out.println("Result");
        System.out.println(new String(w.getMessage()));
    }

    public void test2() {
        System.out.println("---test bmp small---");
        IContainerFile cover = new BmpFileContainer();
        cover.loadFile("src/resources/test.bmp");
        String secretMessage = "this is a secret message this is a secret message";
        int size = secretMessage.length();
        IReadStegoFile readStego = new ReadStegoFileImpl(secretMessage.getBytes());
        IStegoStrategy lsb = new RandomSimpleLSB(4, 42);
        lsb.encode(cover, readStego);
        cover.saveFile("src/resources/testStego1.bmp");
        IContainerFile modifiedCover = new BmpFileContainer();
        modifiedCover.loadFile("src/resources/testStego1.bmp");
        IWriteStegoFile writeStego = new WriteStegoFileImpl(size);
        lsb.decode(modifiedCover, writeStego);
        System.out.println(secretMessage.equals(new String(writeStego.getMessage()))
                ? "PASSED"
                : "FAILED");
        //System.out.println(new String(writeStego.getMessage()));
    }

    public void test3() throws Exception {
        System.out.println("---test bmp big---");
        File file = new File("src/resources/testText.txt");
        InputStream is;
        is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();

        IContainerFile cover = new BmpFileContainer();
        cover.loadFile("src/resources/test.bmp");
        IReadStegoFile readStego = new ReadStegoFileImpl(bytes);
        IStegoStrategy lsb = new RandomSimpleLSB(4, 42);
        lsb.encode(cover, readStego);
        cover.saveFile("src/resources/testStego2.bmp");
        IContainerFile modifiedCover = new BmpFileContainer();
        modifiedCover.loadFile("src/resources/testStego2.bmp");
        IWriteStegoFile writeStego = new WriteStegoFileImpl(bytes.length);
        lsb.decode(modifiedCover, writeStego);
        //System.out.println(new String(writeStego.getMessage()));
        System.out.println((new String(bytes)).equals(new String(writeStego.getMessage()))
                ? "PASSED"
                : "FAILED");
    }

    public void test4() {
        System.out.println("---test wav small---");
        IContainerFile cover = new WavFileContainer();
        cover.loadFile("src/resources/test.wav");
        String secretMessage = "this is a secret message this is a secret message";
        int size = secretMessage.length();
        IReadStegoFile readStego = new ReadStegoFileImpl(secretMessage.getBytes());
        IStegoStrategy lsb = new RandomSimpleLSB(4, 42);
        lsb.encode(cover, readStego);
        cover.saveFile("src/resources/testStego1.wav");
        IContainerFile modifiedCover = new WavFileContainer();
        modifiedCover.loadFile("src/resources/testStego1.wav");
        IWriteStegoFile writeStego = new WriteStegoFileImpl(size);
        lsb.decode(modifiedCover, writeStego);
        //System.out.println(new String(writeStego.getMessage()));
        System.out.println(secretMessage.equals(new String(writeStego.getMessage()))
                ? "PASSED"
                : "FAILED");
    }

    public void test5() throws Exception {
        System.out.println("---test wav big---");
        File file = new File("src/resources/testText.txt");
        InputStream is;
        is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();

        IContainerFile cover = new WavFileContainer();
        cover.loadFile("src/resources/test.wav");
        IReadStegoFile readStego = new ReadStegoFileImpl(bytes);
        IStegoStrategy lsb = new RandomSimpleLSB(4, 42);
        lsb.encode(cover, readStego);
        cover.saveFile("src/resources/testStego2.wav");
        IContainerFile modifiedCover = new WavFileContainer();
        modifiedCover.loadFile("src/resources/testStego2.wav");
        IWriteStegoFile writeStego = new WriteStegoFileImpl(bytes.length);
        lsb.decode(modifiedCover, writeStego);
        //System.out.println(new String(writeStego.getMessage()));
        System.out.println((new String(bytes)).equals(new String(writeStego.getMessage()))
                ? "PASSED"
                : "FAILED");
    }
}
