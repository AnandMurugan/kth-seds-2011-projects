/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.common;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Igor
 */
public class FileInfo implements Serializable {
    private String ownerHost;
    private File file;

    public FileInfo() {
    }

    public FileInfo(String ownerHost, File file) {
        this.ownerHost = ownerHost;
        this.file = file;
    }

    public String getOwnerHost() {
        return ownerHost;
    }

    public String getName() {
        return file.getName();
    }

    public long getSize() {
        return file.length();
    }

    public String getLocalKey() throws IOException {
        return file.getPath();
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

        if (!other.file.equals(this.file)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.ownerHost != null ? this.ownerHost.hashCode() : 0);
        hash = 71 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }
}
