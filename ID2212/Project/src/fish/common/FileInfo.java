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
 * Entity that represents information about shared file in FISH
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
     * Name for query that extracts all objects from DB
     */
    public static final String GET_ALL_FILE_INFO_LIST = "FileInfo_getAllFileInfo";
    /**
     * Name for query that extracts all objects of particular owner from DB
     */
    public static final String GET_FILE_INFO_LIST_BY_OWNER = "FileInfo_getFileInfoListByOwner";
    /**
     * Name for query that deletes all objects of particular owner from DB
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
     * Default constructor
     */
    public FileInfo() {
    }

    /**
     * Creates a new {@code FileInfo} object
     * 
     * @param ownerHost Host of client/peer
     * @param ownerPort Port of client/peer
     * @param fileName File name
     * @param fileSize Size of the file
     * @param localKey Canonical path to the file
     */
    public FileInfo(String ownerHost, int ownerPort, String fileName, long fileSize, String localKey) {
        this.ownerHost = ownerHost;
        this.ownerPort = ownerPort;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.localKey = localKey;
    }

    /**
     * Gets owner host
     * 
     * @return owner host
     */
    public String getOwnerHost() {
        return ownerHost;
    }

    /**
     * Gets owner port
     * 
     * @return owner port
     */
    public int getOwnerPort() {
        return ownerPort;
    }

    /**
     * Gets file name
     * 
     * @return file name
     */
    public String getName() {
        return fileName;
    }

    /**
     * Gets file size
     * 
     * @return file size
     */
    public long getSize() {
        return fileSize;
    }

    /**
     * Gets canonical path to the file
     * 
     * @return canonical path to the file
     */
    public String getLocalKey() {
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
