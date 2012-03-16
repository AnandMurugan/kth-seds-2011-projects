/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package authenticator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.EapolKeyMessage;
import utility.MacAddress;
import utility.Prf;

/**
 *
 * @author Igor
 */
class SupplicantHandler extends Thread {
    private Socket socket;
    private byte[] aMac, sMac;
    private byte[] pmk;
    private byte[] aNonce, sNonce;
    private byte[] ptk;

    public SupplicantHandler(Socket supplicantSocket, MacAddress aMac, MacAddress sMac, byte[] pmk) {
        this.socket = supplicantSocket;
        this.aMac = aMac.getBytes();
        this.sMac = sMac.getBytes();
        this.pmk = pmk;
    }

    @Override
    public void run() {
        try {
            //Setup*************************************************************             
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            Random rand = new Random(System.nanoTime());
            long replayCounter = rand.nextLong();
            byte[] micReceived, mic;
            System.out.println("Starting four-way exchange...");

            //generate aNonce
            long randNr = rand.nextLong();
            ByteBuffer randNrBuf = ByteBuffer.allocate(8);
            randNrBuf.putLong(randNr);
            ByteBuffer timeBuf = ByteBuffer.allocate(8);
            timeBuf.putLong(System.nanoTime());
            aNonce = Prf.prf256(randNrBuf.array(), "Init Counter", aMac, timeBuf.array());
               
            //Message A*********************************************************
            EapolKeyMessage messageA = new EapolKeyMessage();
            messageA.setDescriptorType((byte) 254);
            BitSet keyInformationA = messageA.getKeyInformation();
            keyInformationA.set(4, false); //request: 0
            keyInformationA.set(5, false); //error: 0
            keyInformationA.set(6, false); //secure: 0
            keyInformationA.set(7, false); //MIC: 0
            keyInformationA.set(8, true); //ack: 1
            keyInformationA.set(9, false); //install: 0
            keyInformationA.set(10, false); //
            keyInformationA.set(11, false); //index: 00
            keyInformationA.set(12, true); //key type: pairwise
            keyInformationA.set(13, true); //
            keyInformationA.set(14, false); //
            keyInformationA.set(15, false); //descriptor type: 001
            messageA.setKeyLength(64);
            messageA.setReplayCounter(replayCounter);
            messageA.setKeyNonce(aNonce);
            //EAP-Key IV == 0
            //RSC == 0
            messageA.setKeyIdentifier(0L);
            //MIC == 0
            messageA.setKeyDataLength(0); //we are ignoring this
            //Key Data == we are ignoring this

            System.out.println("Sending message A...");
            System.out.println("\tANonce = " + byteArrayToHexString(aNonce));
            out.write(EapolKeyMessage.toBytes(messageA));

            //Message B*********************************************************
            byte[] bytesB = new byte[EapolKeyMessage.MIN_MESSAGE_SIZE];
            in.read(bytesB);
            EapolKeyMessage messageB = EapolKeyMessage.fromBytes(bytesB);
            System.out.println("Received message B...");

            if (messageB.getReplayCounter() != replayCounter) {
                System.out.println("ERROR: replay attack!");
                socket.close();
                return;
            }
            System.out.println("\tReplay counter: OK");

            sNonce = messageB.getKeyNonce();
            System.out.println("\tSNonce = " + byteArrayToHexString(sNonce));
            
            //generate ptk
            ptk = Prf.prf512(pmk, "Pairwaise key expansion", sMac, aMac, sNonce, aNonce);
            
            micReceived = messageB.getKeyMic();
            mic = Prf.hmacMD5(
                    Arrays.copyOfRange(ptk, 48, 64),
                    Arrays.copyOfRange(EapolKeyMessage.toBytes(messageB), 0, 77));
            if (!Arrays.equals(mic, micReceived)) {
                System.out.println("ERROR: message modified or supplicant is wrong!");
                socket.close();
                return;
            }
            System.out.println("\tMIC: OK");

            //Message C*********************************************************
            EapolKeyMessage messageC = new EapolKeyMessage();
            messageC.setDescriptorType((byte) 254);
            BitSet keyInformationC = messageC.getKeyInformation();
            keyInformationC.set(4, false); //request: 0
            keyInformationC.set(5, false); //error: 0
            keyInformationC.set(6, false); //secure: 0
            keyInformationC.set(7, true); //MIC: 1
            keyInformationC.set(8, true); //ack: 1
            keyInformationC.set(9, false); //install: 0
            keyInformationC.set(10, false); //
            keyInformationC.set(11, false); //index: 00
            keyInformationC.set(12, true); //key type: pairwise
            keyInformationC.set(13, true); //
            keyInformationC.set(14, false); //
            keyInformationC.set(15, false); //descriptor type: 001
            messageC.setKeyLength(64);
            messageC.setReplayCounter(++replayCounter);
            messageC.setKeyNonce(aNonce);
            //EAP-Key IV == 0
            messageC.setKeyRsc(0L); //RSC == Starting Sequence Number
            messageC.setKeyIdentifier(0L);
            mic = Prf.hmacMD5(
                    Arrays.copyOfRange(ptk, 48, 64),
                    Arrays.copyOfRange(EapolKeyMessage.toBytes(messageC), 0, 77));
            messageC.setKeyMic(mic); //MIC == MIC Value
            messageC.setKeyDataLength(0); //we are ignoring this
            //Key Data == we are ignoring this

            System.out.println("Sending message C...");
            System.out.println("\tRSC = " + messageC.getKeyRsc());
            out.write(EapolKeyMessage.toBytes(messageC));

            //Message D*********************************************************
            byte[] bytesD = new byte[EapolKeyMessage.MIN_MESSAGE_SIZE];
            in.read(bytesD);
            EapolKeyMessage messageD = EapolKeyMessage.fromBytes(bytesD);
            System.out.println("Received message D...");

            if (messageD.getReplayCounter() != replayCounter) {
                System.out.println("ERROR: replay attack!");
                socket.close();
                return;
            }
            System.out.println("\tReplay counter: OK");

            micReceived = messageD.getKeyMic();
            mic = Prf.hmacMD5(
                    Arrays.copyOfRange(ptk, 48, 64),
                    Arrays.copyOfRange(EapolKeyMessage.toBytes(messageD), 0, 77));
            if (!Arrays.equals(mic, micReceived)) {
                System.out.println("ERROR: message modified or supplicant is wrong!");
                socket.close();
                return;
            }
            System.out.println("\tMIC: OK");

            //Print PTK*********************************************************
            System.out.println("Four-way exchange is done!");
            System.out.println("\tData Encr Key =\t" + byteArrayToHexString(ptk, 0, 16));
            System.out.println("\tData MIC Key =\t" + byteArrayToHexString(ptk, 16, 16));
            System.out.println("\tEAPOL Encr Key =\t" + byteArrayToHexString(ptk, 32, 16));
            System.out.println("\tEAPOL MIC Key =\t" + byteArrayToHexString(ptk, 48, 16));
        } catch (Exception ex) {
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(SupplicantHandler.class.getName()).log(Level.SEVERE, null, ex);
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
}
