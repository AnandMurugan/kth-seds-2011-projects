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
public class Prf {

    private static byte[] prf128(byte[] key, String textString, byte[] byteSequence, int i) {
        ByteBuffer buf = ByteBuffer.allocate(textString.getBytes().length + 1 + byteSequence.length + 4);
        buf.put(textString.getBytes());
        buf.put((byte) 0x00);
        buf.put(byteSequence);
        buf.putInt(i);
        Key k = new SecretKeySpec(key, "HmacSHA1");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(k);
            ByteBuffer returnBuf = ByteBuffer.allocate(128);
            returnBuf.put(mac.doFinal(buf.array()), 0, 128);
            return returnBuf.array();
        } catch (Exception ex) {
            Logger.getLogger(Prf.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static byte[] prf256(byte[] key, String textString, byte[] mac, byte[] time) {
        ByteBuffer returnBuf = ByteBuffer.allocate(256);
        ByteBuffer byteSeqBuf = ByteBuffer.allocate(mac.length + time.length);
        byteSeqBuf.put(mac);
        byteSeqBuf.put(time);
        byte[] byteSequence = byteSeqBuf.array();
        
        returnBuf.put(prf128(key, textString, byteSequence, 0));
        returnBuf.put(prf128(key, textString, byteSequence, 1));

        return returnBuf.array();
    }

    public static byte[] prf512(byte[] key, String textString, byte[] mac1, byte[] mac2, byte[] nonce1, byte[] nonce2) {
        ByteBuffer returnBuf = ByteBuffer.allocate(512);

        ByteBuffer byteSeqBuf = ByteBuffer.allocate(mac1.length + mac2.length + nonce1.length + nonce2.length);
        ByteBuffer auxBuf1, auxBuf2;
        auxBuf1 = ByteBuffer.allocate(mac1.length);
        auxBuf1.put(mac1);
        auxBuf2 = ByteBuffer.allocate(mac2.length);
        auxBuf2.put(mac2);
        if (auxBuf1.compareTo(auxBuf2) <= 0) {
            byteSeqBuf.put(mac1);
            byteSeqBuf.put(mac2);
        } else {
            byteSeqBuf.put(mac2);
            byteSeqBuf.put(mac1);
        }
        auxBuf1 = ByteBuffer.allocate(nonce1.length);
        auxBuf1.put(nonce1);
        auxBuf2 = ByteBuffer.allocate(nonce2.length);
        auxBuf2.put(nonce2);
        if (auxBuf1.compareTo(auxBuf2) <= 0) {
            byteSeqBuf.put(nonce1);
            byteSeqBuf.put(nonce2);
        } else {
            byteSeqBuf.put(nonce2);
            byteSeqBuf.put(nonce1);
        }
        
        byte[] byteSequence = byteSeqBuf.array();
        returnBuf.put(prf128(key, textString, byteSequence, 0));
        returnBuf.put(prf128(key, textString, byteSequence, 1));

        return returnBuf.array();
    }
}
