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
}
