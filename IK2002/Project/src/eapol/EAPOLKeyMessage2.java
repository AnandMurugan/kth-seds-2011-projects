/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eapol;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * @author Igor
 */
public class EAPOLKeyMessage2 {
    private static final int MIN_MESSAGE_SIZE = 95;
    //
    private byte descriptorType; //1 byte
    private BitSet keyInformation; //2 bytes
    private int keyLength; //2 bytes
    private long replayCounter; // 8 bytes
    private byte[] keyNonce; //32 bytes
    private byte[] eapolKeyIv; //16 bytes
    private long keyRsc; //8 bytes
    private long keyIdentifier; //8 bytes
    private byte[] keyMic; //16 bytes
    private int keyDataLength; //4 bytes
    private byte[] keyData; //0...n bytes

    public static byte[] toBytes(EAPOLKeyMessage2 message) {
        ByteBuffer outBuffer = ByteBuffer.allocate(MIN_MESSAGE_SIZE + message.keyDataLength);
        ByteBuffer aux;

        outBuffer.put(message.descriptorType);

        aux = ByteBuffer.allocate(2);
        aux.put(message.keyInformation.toByteArray());
        outBuffer.put(aux.array(), 0, 2);
        System.out.println(Arrays.toString(aux.array()));

        aux = ByteBuffer.allocate(4);
        aux.putInt(message.keyLength);
        outBuffer.put(aux.array(), 2, 2);

        outBuffer.putLong(message.replayCounter);

        outBuffer.put(message.keyNonce, 0, 32);

        outBuffer.put(message.eapolKeyIv, 0, 16);

        outBuffer.putLong(message.keyRsc);

        outBuffer.putLong(message.keyIdentifier);

        outBuffer.put(message.keyMic, 0, 16);

        aux = ByteBuffer.allocate(4);
        aux.putInt(message.keyDataLength);
        outBuffer.put(aux.array(), 2, 2);

        outBuffer.put(message.keyData, 0, message.keyDataLength);

        return outBuffer.array();
    }

    public static EAPOLKeyMessage2 fromBytes(byte[] bytes) {
        ByteBuffer inBuffer = ByteBuffer.wrap(bytes);
        ByteBuffer aux;
        byte[] buf;
        EAPOLKeyMessage2 message = new EAPOLKeyMessage2();

        message.descriptorType = inBuffer.get();

        buf = new byte[2];
        inBuffer.get(buf);
        message.keyInformation = BitSet.valueOf(buf);
        System.out.println(Arrays.toString(buf));

        buf = new byte[4];
        inBuffer.get(buf, 2, 2);
        aux = ByteBuffer.wrap(buf);
        message.keyLength = aux.getInt();

        message.replayCounter = inBuffer.getLong();

        message.keyNonce = new byte[32];
        inBuffer.get(message.keyNonce, 0, 32);

        message.eapolKeyIv = new byte[16];
        inBuffer.get(message.eapolKeyIv, 0, 16);

        message.keyRsc = inBuffer.getLong();

        message.keyIdentifier = inBuffer.getLong();

        message.keyMic = new byte[16];
        inBuffer.get(message.keyMic, 0, 16);

        buf = new byte[4];
        inBuffer.get(buf, 2, 2);
        aux = ByteBuffer.wrap(buf);
        message.keyDataLength = aux.getInt();

        message.keyData = new byte[message.keyDataLength];
        inBuffer.get(message.keyData, 0, message.keyDataLength);

        return message;
    }

    @Override
    public String toString() {
        return "EapolKeyMessage{"
                + "\n\tdescriptorType=" + descriptorType
                + ", \n\tkeyInformation=" + keyInformation
                + ", \n\tkeyLength=" + keyLength
                + ", \n\treplayCounter=" + replayCounter
                + ", \n\tkeyNonce=" + keyNonce
                + ", \n\teapolKeyIv=" + eapolKeyIv
                + ", \n\tkeyRsc=" + keyRsc
                + ", \n\tkeyIdentifier=" + keyIdentifier
                + ", \n\tkeyMic=" + keyMic
                + ", \n\tkeyDataLength=" + keyDataLength
                + ", \n\tkeyData=" + keyData
                + "\n}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EAPOLKeyMessage2 other = (EAPOLKeyMessage2) obj;
        if (this.descriptorType != other.descriptorType) {
            return false;
        }
        if (this.keyInformation != other.keyInformation && (this.keyInformation == null || !this.keyInformation.equals(other.keyInformation))) {
            return false;
        }
        if (this.keyLength != other.keyLength) {
            return false;
        }
        if (this.replayCounter != other.replayCounter) {
            return false;
        }
        if (!Arrays.equals(this.keyNonce, other.keyNonce)) {
            return false;
        }
        if (!Arrays.equals(this.eapolKeyIv, other.eapolKeyIv)) {
            return false;
        }
        if (this.keyRsc != other.keyRsc) {
            return false;
        }
        if (this.keyIdentifier != other.keyIdentifier) {
            return false;
        }
        if (!Arrays.equals(this.keyMic, other.keyMic)) {
            return false;
        }
        if (this.keyDataLength != other.keyDataLength) {
            return false;
        }
        if (!Arrays.equals(this.keyData, other.keyData)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.descriptorType;
        hash = 73 * hash + (this.keyInformation != null ? this.keyInformation.hashCode() : 0);
        hash = 73 * hash + this.keyLength;
        hash = 73 * hash + (int) (this.replayCounter ^ (this.replayCounter >>> 32));
        hash = 73 * hash + Arrays.hashCode(this.keyNonce);
        hash = 73 * hash + Arrays.hashCode(this.eapolKeyIv);
        hash = 73 * hash + (int) (this.keyRsc ^ (this.keyRsc >>> 32));
        hash = 73 * hash + (int) (this.keyIdentifier ^ (this.keyIdentifier >>> 32));
        hash = 73 * hash + Arrays.hashCode(this.keyMic);
        hash = 73 * hash + this.keyDataLength;
        hash = 73 * hash + Arrays.hashCode(this.keyData);
        return hash;
    }

    public static void main(String[] args) {
        EAPOLKeyMessage2 m1 = new EAPOLKeyMessage2();
        m1.descriptorType = 1;
        m1.keyInformation = new BitSet(16);
        //m1.keyInformation.flip(0, 8);
        m1.keyLength = 42;
        m1.replayCounter = 123456L;
        m1.keyNonce = new byte[32];
        m1.eapolKeyIv = new byte[16];
        m1.keyRsc = 789456L;
        m1.keyIdentifier = 0L;
        m1.keyMic = new byte[16];
        m1.keyDataLength = 0;
        m1.keyData = new byte[0];

        byte[] bytes = EAPOLKeyMessage2.toBytes(m1);
        System.out.println(Arrays.toString(bytes));

        EAPOLKeyMessage2 m2 = EAPOLKeyMessage2.fromBytes(bytes);

        System.out.println(m1 + "\n\n" + m2);

        System.out.println("\nResult: " + m1.equals(m2));
    }
}