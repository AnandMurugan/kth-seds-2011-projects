/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 *
 * @author Igor
 */
public class MacAddress {
    private byte[] bytes;

    public MacAddress(byte[] address) throws Exception {
        if (address.length != 6) {
            throw new Exception();
        }
        this.bytes = address;
    }

    public MacAddress(String address) throws Exception {
        StringTokenizer st = new StringTokenizer(address, "-");
        int length = st.countTokens();
        if (length != 6) {
            throw new Exception();
        }
        for (int i = 0; i < length; i++) {
            bytes[i] = Byte.parseByte(st.nextToken(), 16);
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MacAddress other = (MacAddress) obj;
        if (!Arrays.equals(this.bytes, other.bytes)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Arrays.hashCode(this.bytes);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(17);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x%s", bytes[i], (i < bytes.length - 1) ? "-" : ""));
        }

        return sb.toString();
    }
}
