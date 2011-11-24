/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author julio
 */
@NamedQueries({
    @NamedQuery(name = CatalogFile.GET_USER_FILES_QUERY,
    query = "SELECT f "
    + "FROM CatalogFile f "
    + "WHERE f.owner = :owner"),
    @NamedQuery(name = CatalogFile.GET_USER_PRIVATE_FILES_QUERY,
    query = "SELECT f "
    + "FROM CatalogFile f "
    + "WHERE f.owner = :owner AND f.accessPermission = model.AccessPermission.PRIVATE"),
    @NamedQuery(name = CatalogFile.GET_PUBLIC_FILES_QUERY,
    query = "SELECT f "
    + "FROM CatalogFile f "
    + "WHERE f.accessPermission = model.AccessPermission.PUBLIC"),
    @NamedQuery(name = CatalogFile.GET_AVAILABLE_FILES_QUERY,
    query = "SELECT f "
    + "FROM CatalogFile f "
    + "WHERE "
    + "(f.accessPermission = model.AccessPermission.PUBLIC AND NOT (f.owner = :owner))"
    + "OR"
    + "(f.owner = :owner)"),
    @NamedQuery(name = CatalogFile.GET_FILE_BY_ID_QUERY,
    query = "SELECT f "
    + "FROM CatalogFile f "
    + "WHERE f.id = :id"),
    @NamedQuery(name = CatalogFile.UPDATE_FILE_QUERY,
    query = "UPDATE CatalogFile f "
    + "SET "
    + "f.lastModifiedTime = :newTime, "
    + "f.fileSize = :newSize " 
    + "WHERE f.id = :id"),
    @NamedQuery(name = CatalogFile.DELETE_FILE_QUERY,
    query = "DELETE "
    + "FROM CatalogFile f "
    + "WHERE f.id = :id")
})
@Entity
public class CatalogFile implements Serializable {
    public static final String GET_USER_FILES_QUERY = "CatalogFile_getUserFiles";
    public static final String GET_USER_PRIVATE_FILES_QUERY = "CatalogFile_getUserPrivateFiles";
    public static final String GET_PUBLIC_FILES_QUERY = "CatalogFile_getPublicFiles";
    public static final String GET_AVAILABLE_FILES_QUERY = "CatalogFile_getAvailableFiles";
    public static final String GET_FILE_BY_ID_QUERY = "CatalogFile_getFile";
    public static final String UPDATE_FILE_QUERY = "CatalogFile_updateFile";
    public static final String DELETE_FILE_QUERY = "CatalogFile_deleteFile";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "flie_name", nullable = false)
    private String fileName;
    @Column(name = "file_size", nullable = false)
    private long fileSize;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private CatalogUser owner;
    @Enumerated(EnumType.STRING)
    @Column(name = "access", nullable = false)
    private AccessPermission accessPermission;
    @Temporal(TemporalType.DATE)
    @Column(name = "last_modification_time", nullable = false)
    private Date lastModifiedTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "write_read")
    private WriteReadPermission writeReadPermission;
    @Column(name = "file_path", nullable = false)
    private String filePath;

    public CatalogFile() {
    }

    public CatalogFile(String fileName, long fileSize, CatalogUser owner, AccessPermission accessPermission, Date lastModifiedTime, WriteReadPermission writeReadPermission, String filePath) {
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

    public long getFileSize() {
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

    public void setFileSize(long fileSize) {
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
//    public static void main(String[] args) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(CatalogFile.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        EntityManager em = Persistence.createEntityManagerFactory("model").createEntityManager();
//
//        EntityTransaction transaction = null;
//        try {
//            transaction = beginTransaction(em);
//
//            CatalogUser user = new CatalogUser("igor", "pass".hashCode());
//            em.persist(user);
//
//            for (int i = 0; i < 10; i++) {
//                CatalogFile file = new CatalogFile(
//                        "file.fil",
//                        i,
//                        user,
//                        AccessPermission.PUBLIC,
//                        new Date(System.currentTimeMillis()),
//                        WriteReadPermission.WRITE,
//                        "file.fil");
//                em.persist(file);
//
//                List<CatalogFile> files = em.createNamedQuery("getAllPublicFiles", CatalogFile.class).getResultList();
//                System.out.println(files.toArray(new CatalogFile[1]).toString());
//            }
//        } finally {
//            commitTransaction(transaction);
//        }
//    }
//
//    private static EntityTransaction beginTransaction(EntityManager em) {
//        EntityTransaction transaction = em.getTransaction();
//        transaction.begin();
//        return transaction;
//    }
//
//    private static void commitTransaction(EntityTransaction transaction) {
//        transaction.commit();
//    }
}
