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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author julio
 */
@Entity(name = "Files")
public class CatalogFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private int id;
    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;
    @Column(name = "FILE_SIZE", nullable = false)
    private int fileSize;
    @ManyToOne
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private CatalogUser owner;
    @Enumerated(EnumType.STRING)
    @Column(name = "ACCESS", nullable = false)
    private AccessPermission accessPermission;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_MODIFICATION_TIME", nullable = false)
    private Date lastModifiedTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "WRITE_READ", nullable = false)
    private WriteReadPermission writeReadPermission;
    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    public CatalogFile() {
    }

    public CatalogFile(String fileName, int fileSize, CatalogUser owner, AccessPermission accessPermission, Date lastModifiedTime, WriteReadPermission writeReadPermission, String filePath) {
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
//
//            // create account.
//            CatalogUser user = new CatalogUser("igor", "pass".hashCode());
//            em.persist(user);
//            //List<CatalogFile> userFiles = new ArrayList<CatalogFile>();
//            for (int i = 0; i < 1; i++) {
//                CatalogFile file = new CatalogFile(
//                        "file.fil",
//                        100,
//                        user,
//                        AccessPermission.PUBLIC,
//                        new Date(System.currentTimeMillis()),
//                        WriteReadPermission.WRITE,
//                        "file.fil");
//                em.persist(file);
//            }
//            //user.setMyCatalogFiles(userFiles);
//            //CatalogFile file = new CatalogFile("file.fil", 100, user, AccessPermission.PUBLIC, new Date(System.currentTimeMillis()), WriteReadPermission.WRITE, "file.fil");
//            //em.persist(file);
//            //em.persist(user);
//
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
