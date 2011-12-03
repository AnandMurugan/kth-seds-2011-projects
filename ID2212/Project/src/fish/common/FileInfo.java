/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.common;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 *
 * @author Igor
 */
public class FileInfo {
    private InetSocketAddress ownerAddress;
    private File file;

    public FileInfo() {
    }

    public FileInfo(InetSocketAddress ownerAddress, File file) {
        this.ownerAddress = ownerAddress;
        this.file = file;
    }

    public InetSocketAddress getOwnerAddress() {
        return ownerAddress;
    }

    public String getName() {
        return file.getName();
    }

    public long getSize() {
        return file.length();
    }

    public String getCanonicalPath() throws IOException {
        return file.getCanonicalPath();
    }
}
