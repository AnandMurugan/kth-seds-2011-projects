/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author julio
 */
@Entity
public class CatalogFile implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String fileName;
    private int fileSize;
    @ManyToOne
    private CatalogUser owner;
    @Enumerated(EnumType.STRING)
    private AccessPermission accessPermission;
    @Temporal(TemporalType.DATE)
    private Date lastModifiedTime;
    @Enumerated(EnumType.STRING)
    private WriteReadPermission writeReadPermission;
    private String filePath;

    public CatalogFile() {
    }

    public CatalogFile(int id, String fileName, int fileSize, CatalogUser owner, AccessPermission accessPermission, Date lastModifiedTime, WriteReadPermission writeReadPermission, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.owner = owner;
        this.accessPermission = accessPermission;
        this.lastModifiedTime = lastModifiedTime;
        this.writeReadPermission = writeReadPermission;
        this.filePath = filePath;
    }

    public AccessPermission getAccessPermission() {
        return accessPermission;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getId() {
        return id;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public CatalogUser getOwner() {
        return owner;
    }

    public WriteReadPermission getWriteReadPermission() {
        return writeReadPermission;
    }

    public void setAccessPermission(AccessPermission accessPermission) {
        this.accessPermission = accessPermission;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public void setOwner(CatalogUser owner) {
        this.owner = owner;
    }

    public void setWriteReadPermission(WriteReadPermission writeReadPermission) {
        this.writeReadPermission = writeReadPermission;
    }
}
