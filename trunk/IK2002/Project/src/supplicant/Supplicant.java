/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supplicant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.EapolKeyMessage;
import utility.MacAddress;
import utility.CryptoSuite;

/**
 *
 * @author Alex
 */
public class Supplicant {
    private static final int DEFAULT_PORT = 8080;
    private static Map<MacAddress, byte[]> macToPmk = new HashMap<MacAddress, byte[]>();

    {
        try {
            macToPmk.put(new MacAddress("70-f1-a1-3f-a0-f7"), "1234567812345678".getBytes());
            macToPmk.put(new MacAddress("00-23-4d-d3-63-b0"), "1234567812345678".getBytes());
            macToPmk.put(new MacAddress("00-1f-16-43-e0-db"), "1234567812345678".getBytes());
        } catch (Exception ex) {
            Logger.getLogger(Supplicant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private Socket socket;
    private byte[] aMac, sMac;
    private byte[] pmk;
    private byte[] aNonce, sNonce;
    private byte[] ptk;

    public void authenticate(String ip, int port) {
        connect(ip, port);
        exchangeMessages();
    }

    private void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Authenticator connection started...");
            MacAddress authenticatorMac = new MacAddress(
                    NetworkInterface.getByInetAddress(socket.getInetAddress()).getHardwareAddress());
            aMac = authenticatorMac.getBytes();
            sMac = new MacAddress(
                    NetworkInterface.getByInetAddress(socket.getLocalAddress()).getHardwareAddress()).getBytes();
            pmk = macToPmk.get(authenticatorMac);
        } catch (Exception ex) {
            Logger.getLogger(Supplicant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exchangeMessages() {
        try {
            //Setup*************************************************************             
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            Random rand = new Random(System.nanoTime());
            long replayCounter;
            byte[] micReceived, mic;
            System.out.println("Starting four-way exchange...");

            //generate sNonce
            ByteBuffer keyBuf = ByteBuffer.allocate(8);
            keyBuf.putLong(rand.nextLong());
            sNonce = CryptoSuite.prf(256, keyBuf.array(), "Init Counter", CryptoSuite.generateByteSeqForNonce(sMac));

            //Message A*********************************************************
            byte[] bytesA = new byte[EapolKeyMessage.MIN_MESSAGE_SIZE];
            in.read(bytesA);
            EapolKeyMessage messageA = EapolKeyMessage.fromBytes(bytesA);
            System.out.println("Received message A...");

            replayCounter = messageA.getReplayCounter();

            aNonce = messageA.getKeyNonce();
            System.out.println("\tANonce = " + byteArrayToHexString(aNonce));

            //generate ptk
            ptk = CryptoSuite.prf(512, pmk, "Pairwaise key expansion", CryptoSuite.generateByteSeqForPTK(aMac, sMac, aNonce, sNonce));

            //Message B*********************************************************
            EapolKeyMessage messageB = new EapolKeyMessage();
            messageB.setDescriptorType((byte) 254);
            BitSet keyInformationB = messageB.getKeyInformation();
            keyInformationB.set(4, false); //request: 0
            keyInformationB.set(5, false); //error: 0
            keyInformationB.set(6, false); //secure: 0
            keyInformationB.set(7, true); //MIC: 1
            keyInformationB.set(8, false); //ack: 0
            keyInformationB.set(9, false); //install: 0
            keyInformationB.set(10, false); //
            keyInformationB.set(11, false); //index: 0
            keyInformationB.set(12, true); //key type: pairwise
            keyInformationB.set(13, true); //
            keyInformationB.set(14, false); //
            keyInformationB.set(15, false); //descriptor type: 001
            messageB.setKeyLength(64);
            messageB.setReplayCounter(replayCounter);
            messageB.setKeyNonce(sNonce);
            //EAP-Key IV == 0
            //RSC == 0
            messageB.setKeyIdentifier(0L);
            mic = CryptoSuite.hmacMD5(
                    Arrays.copyOfRange(ptk, 48, 64),
                    Arrays.copyOfRange(EapolKeyMessage.toBytes(messageB), 0, 77));
            messageB.setKeyMic(mic);
            messageB.setKeyDataLength(0); //we are ignoring this
            //Key Data == we are ignoring this

            System.out.println("Sending message B...");
            System.out.println("\tSNonce = " + byteArrayToHexString(sNonce));
            out.write(EapolKeyMessage.toBytes(messageB));

            //Message C*********************************************************
            byte[] bytesC = new byte[EapolKeyMessage.MIN_MESSAGE_SIZE];
            in.read(bytesC);
            EapolKeyMessage messageC = EapolKeyMessage.fromBytes(bytesC);
            System.out.println("Received message C...");

            if (messageC.getReplayCounter() != ++replayCounter) {
                System.out.println("ERROR: replay attack!");
                socket.close();
                return;
            }
            System.out.println("\tReplay counter: OK");

            micReceived = messageC.getKeyMic();
            mic = CryptoSuite.hmacMD5(
                    Arrays.copyOfRange(ptk, 48, 64),
                    Arrays.copyOfRange(EapolKeyMessage.toBytes(messageC), 0, 77));
            if (!Arrays.equals(mic, micReceived)) {
                System.out.println("ERROR: message modified or supplicant is wrong!");
                socket.close();
                return;
            }
            System.out.println("\tMIC: OK");


            //Message D*********************************************************
            EapolKeyMessage messageD = new EapolKeyMessage();
            messageD.setDescriptorType((byte) 254);
            BitSet keyInformationD = messageD.getKeyInformation();
            keyInformationD.set(4, false); //request: 0
            keyInformationD.set(5, false); //error: 0
            keyInformationD.set(6, false); //secure: 0
            keyInformationD.set(7, true); //MIC: 1
            keyInformationD.set(8, false); //ack: 0
            keyInformationD.set(9, true); //install: 1
            keyInformationD.set(10, false); //
            keyInformationD.set(11, false); //index: 0
            keyInformationD.set(12, true); //key type: pairwise
            keyInformationD.set(13, true); //
            keyInformationD.set(14, false); //
            keyInformationD.set(15, false); //descriptor type: 001
            messageD.setKeyLength(64);
            messageD.setReplayCounter(replayCounter);
            messageD.setKeyNonce(sNonce);
            //EAP-Key IV == 0
            //RSC == 0
            messageD.setKeyIdentifier(0L);
            mic = CryptoSuite.hmacMD5(
                    Arrays.copyOfRange(ptk, 48, 64),
                    Arrays.copyOfRange(EapolKeyMessage.toBytes(messageD), 0, 77));
            messageD.setKeyMic(mic);
            messageD.setKeyDataLength(0); //we are ignoring this
            //Key Data == we are ignoring this

            System.out.println("Sending message D...");
            //System.out.println("\tSNonce = " + byteArrayToHexString(sNonce));
            out.write(EapolKeyMessage.toBytes(messageD));

            //Print PTK*********************************************************
            System.out.println("Four-way exchange is done!");
            System.out.println("\tData Encr Key\t= " + byteArrayToHexString(ptk, 0, 16));
            System.out.println("\tData MIC Key\t= " + byteArrayToHexString(ptk, 16, 16));
            System.out.println("\tEAPOL Encr Key\t= " + byteArrayToHexString(ptk, 32, 16));
            System.out.println("\tEAPOL MIC Key\t= " + byteArrayToHexString(ptk, 48, 16));
        } catch (Exception ex) {
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Supplicant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String byteArrayToHexString(byte[] bytes, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; (i < bytes.length) && (i < offset + length); i++) {
            sb.append(String.format("%02x", bytes[i]));
        }

        return sb.toString();
    }

    private String byteArrayToHexString(byte[] bytes) {
        return byteArrayToHexString(bytes, 0, bytes.length);
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        String ip = "127.0.0.1";

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                ip = args[1];
            } catch (NumberFormatException ex) {
                Logger.getLogger(Supplicant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Suplicant is running...");

        Supplicant s = new Supplicant();
        s.authenticate(ip, port);
    }
}
