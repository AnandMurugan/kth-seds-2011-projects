/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.common;

import java.io.IOException;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Igor
 */
@NamedQueries({
    @NamedQuery(name = FileInfo.GET_ALL_FILE_INFO_LIST,
    query = "SELECT f "
    + "FROM FileInfo f"),
    @NamedQuery(name = FileInfo.GET_FILE_INFO_LIST_BY_OWNER,
    query = "SELECT f "
    + "FROM FileInfo f "
    + "WHERE f.ownerHost = :ownerHost AND f.ownerPort = :ownerPort"),
    @NamedQuery(name = FileInfo.DELETE_ALL_FILE_INFO_DATA_BY_OWNER,
    query = "DELETE FROM FileInfo f WHERE f.ownerHost = :ownerHost AND f.ownerPort = :ownerPort")
})
@Entity
public class FileInfo implements Serializable {
    /**
     * 
     */
    public static final String GET_ALL_FILE_INFO_LIST = "FileInfo_getAllFileInfo";
    /**
     * 
     */
    public static final String GET_FILE_INFO_LIST_BY_OWNER = "FileInfo_getFileInfoListByOwner";
    /**
     * 
     */
    public static final String DELETE_ALL_FILE_INFO_DATA_BY_OWNER = "FileInfo_deleteAllFileInfoDataByOwner";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "owner_host", nullable = false)
    private String ownerHost;
    @Column(name = "owner_port", nullable = false)
    private int ownerPort;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_size", nullable = false)
    private long fileSize;
    @Column(name = "local_key", nullable = false)
    private String localKey;

    /**
     * 
     */
    public FileInfo() {
    }

    /**
     * 
     * @param ownerHost
     * @param ownerPort
     * @param fileName
     * @param fileSize
     * @param localKey
     */
    public FileInfo(String ownerHost, int ownerPort, String fileName, long fileSize, String localKey) {
        this.ownerHost = ownerHost;
        this.ownerPort = ownerPort;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.localKey = localKey;
    }

    /**
     * 
     * @return
     */
    public String getOwnerHost() {
        return ownerHost;
    }

    /**
     * 
     * @return
     */
    public int getOwnerPort() {
        return ownerPort;
    }

    /**
     * 
     * @return
     */
    public String getName() {
        return fileName;
    }

    /**
     * 
     * @return
     */
    public long getSize() {
        return fileSize;
    }

    /**
     * 
     * @return
     * @throws IOException
     */
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

        if (other.ownerPort != this.ownerPort) {
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
        hash = 83 * hash + (this.ownerHost != null ? this.ownerHost.hashCode() : 0);
        hash = 83 * hash + this.ownerPort;
        hash = 83 * hash + (this.localKey != null ? this.localKey.hashCode() : 0);
        return hash;
    }
}
