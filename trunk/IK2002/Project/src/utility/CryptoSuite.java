/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.nio.ByteBuffer;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Alex
 */
public class CryptoSuite {
    public static byte[] prf(int n, byte[] key, String text, byte[] byteSequence) {
        try {
            ByteBuffer returnBuf = ByteBuffer.allocate(n / 8);

            Key k = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(k);

            ByteBuffer buf = ByteBuffer.allocate(text.getBytes("US-ASCII").length + 1 + byteSequence.length + 1);
            buf.put(text.getBytes("US-ASCII"));
            buf.put((byte) 0x00);
            buf.put(byteSequence);
            byte i = 0;
            while (i * 160 < n) {
                buf.put(buf.capacity() - 1, i++);
                byte[] data = mac.doFinal(buf.array());
                returnBuf.put(data, 0, (returnBuf.remaining() > 20) ? 20 : returnBuf.remaining());
            }

            return returnBuf.array();
        } catch (Exception ex) {
            Logger.getLogger(CryptoSuite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

//    private static byte[] prf128(byte[] key, String textString, byte[] byteSequence, int i) {
//        ByteBuffer buf = ByteBuffer.allocate(textString.getBytes().length + 1 + byteSequence.length + 1);
//        buf.put(textString.getBytes());
//        buf.put((byte) 0x00);
//        buf.put(byteSequence);
//        buf.put((byte) i);
//        Key k = new SecretKeySpec(key, "HmacSHA1");
//        Mac mac;
//        try {
//            mac = Mac.getInstance("HmacSHA1");
//            mac.init(k);
//            ByteBuffer returnBuf = ByteBuffer.allocate(128);
//            returnBuf.put(mac.doFinal(buf.array()), 0, 128);
//            return returnBuf.array();
//        } catch (Exception ex) {
//            Logger.getLogger(Prf.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public static byte[] prf256(byte[] key, String textString, byte[] mac, byte[] time) {
//        ByteBuffer returnBuf = ByteBuffer.allocate(256);
//        ByteBuffer byteSeqBuf = ByteBuffer.allocate(mac.length + time.length);
//        byteSeqBuf.put(mac);
//        byteSeqBuf.put(time);
//        byte[] byteSequence = byteSeqBuf.array();
//
//        returnBuf.put(prf128(key, textString, byteSequence, 0));
//        returnBuf.put(prf128(key, textString, byteSequence, 1));
//
//        return returnBuf.array();
//    }
//
//    public static byte[] prf512(byte[] key, String textString, byte[] mac1, byte[] mac2, byte[] nonce1, byte[] nonce2) {
//        ByteBuffer returnBuf = ByteBuffer.allocate(512);
//
//        ByteBuffer byteSeqBuf = ByteBuffer.allocate(mac1.length + mac2.length + nonce1.length + nonce2.length);
//        ByteBuffer auxBuf1, auxBuf2;
//        auxBuf1 = ByteBuffer.allocate(mac1.length);
//        auxBuf1.put(mac1);
//        auxBuf2 = ByteBuffer.allocate(mac2.length);
//        auxBuf2.put(mac2);
//        if (auxBuf1.compareTo(auxBuf2) <= 0) {
//            byteSeqBuf.put(mac1);
//            byteSeqBuf.put(mac2);
//        } else {
//            byteSeqBuf.put(mac2);
//            byteSeqBuf.put(mac1);
//        }
//        auxBuf1 = ByteBuffer.allocate(nonce1.length);
//        auxBuf1.put(nonce1);
//        auxBuf2 = ByteBuffer.allocate(nonce2.length);
//        auxBuf2.put(nonce2);
//        if (auxBuf1.compareTo(auxBuf2) <= 0) {
//            byteSeqBuf.put(nonce1);
//            byteSeqBuf.put(nonce2);
//        } else {
//            byteSeqBuf.put(nonce2);
//            byteSeqBuf.put(nonce1);
//        }
//
//        byte[] byteSequence = byteSeqBuf.array();
//        returnBuf.put(prf128(key, textString, byteSequence, 0));
//        returnBuf.put(prf128(key, textString, byteSequence, 1));
//
//        return returnBuf.array();
//    }
    public static byte[] hmacMD5(byte[] key, byte[] message) {
        try {
            Key k = new SecretKeySpec(key, "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(k);
            return mac.doFinal(message);
        } catch (Exception ex) {
            return null;
        }
    }

    public static byte[] generateByteSeqForPTK(byte[] aMac, byte[] sMac, byte[] aNonce, byte[] sNonce) {
        ByteBuffer byteSeqBuf = ByteBuffer.allocate(76);
        ByteBuffer auxBuf1, auxBuf2;
        auxBuf1 = ByteBuffer.allocate(aMac.length);
        auxBuf1.put(aMac);
        auxBuf1.rewind();
        auxBuf2 = ByteBuffer.allocate(sMac.length);
        auxBuf2.put(sMac);
        auxBuf2.rewind();
        if (auxBuf1.compareTo(auxBuf2) <= 0) {
            byteSeqBuf.put(aMac);
            byteSeqBuf.put(sMac);
        } else {
            byteSeqBuf.put(sMac);
            byteSeqBuf.put(aMac);
        }
        auxBuf1 = ByteBuffer.allocate(aNonce.length);
        auxBuf1.put(aNonce);
        auxBuf2 = ByteBuffer.allocate(sNonce.length);
        auxBuf2.put(sNonce);
        if (auxBuf1.compareTo(auxBuf2) <= 0) {
            byteSeqBuf.put(aNonce);
            byteSeqBuf.put(sNonce);
        } else {
            byteSeqBuf.put(sNonce);
            byteSeqBuf.put(aNonce);
        }

        return byteSeqBuf.array();
    }

    public static byte[] generateByteSeqForNonce(byte[] mac) {
        ByteBuffer byteSeqBuf = ByteBuffer.allocate(14);
        byteSeqBuf.put(mac);
        byteSeqBuf.putLong(System.nanoTime());

        return byteSeqBuf.array();
    }
}
