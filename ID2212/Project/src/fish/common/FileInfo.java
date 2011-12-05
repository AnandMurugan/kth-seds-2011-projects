/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.common;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Igor
 */
public class FileInfo implements Serializable {
    private String ownerHost;
    private String fileName;
    private long fileSize;
    private String localKey;

    public FileInfo() {
    }

    public FileInfo(String ownerHost, String fileName, long fileSize, String localKey) {
        this.ownerHost = ownerHost;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.localKey = localKey;
    }

    public String getOwnerHost() {
        return ownerHost;
    }

    public String getName() {
        return fileName;
    }

    public long getSize() {
        return fileSize;
    }

    public String getLocalKey() throws IOException {
        return localKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FileInfo)) {
            return false;
        }

        FileInfo other = (FileInfo) obj;

        if (!other.ownerHost.equals(this.ownerHost)) {
            return false;
        }

        if (!other.localKey.equals(this.localKey)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.ownerHost != null ? this.ownerHost.hashCode() : 0);
        hash = 67 * hash + (this.localKey != null ? this.localKey.hashCode() : 0);
        return hash;
    }
}
