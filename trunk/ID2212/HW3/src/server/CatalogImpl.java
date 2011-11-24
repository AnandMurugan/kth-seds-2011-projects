/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import model.AccessPermission;
import model.CatalogFile;
import model.CatalogUser;
import model.WriteReadPermission;
import utils.RejectedException;

/**
 *
 * @author Igor
 */
public class CatalogImpl extends UnicastRemoteObject implements Catalog {
    private final static String FOLDER = "catalog/";
    private final EntityManager em = Persistence.createEntityManagerFactory("model").createEntityManager();
    private Set<Integer> loggedInUsers = new HashSet<Integer>();

    public CatalogImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerUser(String name, String password) throws RejectedException, RemoteException {
        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction();

            if (password.length() < 8) {
                throw new RejectedException("Password is too short (at least 8 symbols)");
            }

            List<CatalogUser> users = em.createNamedQuery(CatalogUser.GET_USER_BY_NAME_QUERY, CatalogUser.class).
                    setParameter("name", name).
                    getResultList();
            if (!users.isEmpty()) {
                throw new RejectedException("Account with name [" + name + "] already exists!");
            }

            CatalogUser newUser = new CatalogUser(name, password.hashCode());
            em.persist(newUser);
        } finally {
            commitTransaction(transaction);
        }
    }

    @Override
    public void unregisterUser(int id) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction();

            CatalogUser user;
            try {
                user = em.createNamedQuery(CatalogUser.GET_USER_BY_ID_QUERY, CatalogUser.class).
                        setParameter("id", id).
                        getSingleResult();
            } catch (NoResultException ex) {
                user = null;
            }

            if (user != null) {
                List<CatalogFile> privateFiles = em.createNamedQuery(CatalogFile.GET_USER_PRIVATE_FILES_QUERY, CatalogFile.class).
                        setParameter("owner", user).
                        getResultList();

                for (CatalogFile privateFile : privateFiles) {
                    em.remove(privateFile);
                }
                em.remove(user);
            }
        } finally {
            commitTransaction(transaction);
        }
    }

    @Override
    public int login(String name, String password) throws RejectedException, RemoteException {
//        EntityTransaction transaction = null;
//        try {
//            transaction = beginTransaction();
        CatalogUser user;
        try {
            user = em.createNamedQuery(CatalogUser.GET_USER_BY_NAME_QUERY, CatalogUser.class).
                    setParameter("name", name).
                    getSingleResult();
        } catch (NoResultException ex) {
            user = null;
        }

        if ((user == null) || (user.getPassword() != password.hashCode())) {
            throw new RejectedException("Wrong user name or password");
        }

        if (loggedInUsers.add(user.getId()) == false) {
            throw new RejectedException("Already logged in!");
        } else {
            return user.getId();
        }
//        } finally {
//            commitTransaction(transaction);
//        }
    }

    @Override
    public void logout(int id) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

//        EntityTransaction transaction = null;
//        try {
//            transaction = beginTransaction();
        loggedInUsers.remove(id);
//        } finally {
//            commitTransaction(transaction);
//        }
    }

    @Override
    public void uploadFile(int id, String name, AccessPermission access, WriteReadPermission writeRead, byte[] file) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction();

            if ((name == null) || (access == null) || (file == null)) {
                throw new RejectedException("Something is NULL!");
            }

            String uniqueFileName = UUID.randomUUID().toString();
            String filePath = FOLDER + uniqueFileName;

            saveFile(file, filePath);

            CatalogUser user;
            try {
                user = em.createNamedQuery(CatalogUser.GET_USER_BY_ID_QUERY, CatalogUser.class).
                        setParameter("id", id).
                        getSingleResult();
            } catch (NoResultException ex) {
                user = null;
            }

            if (user == null) {
                throw new RejectedException("User not found!");
            }

            CatalogFile catalogFile = new CatalogFile(name, file.length, user, access,
                    new Date(System.currentTimeMillis()), writeRead, filePath);
            em.persist(catalogFile);
        } catch (IOException ex) {
            throw new RejectedException("Bad file!");
        } finally {
            commitTransaction(transaction);
        }
    }

    @Override
    public byte[] downloadFile(int id, int fileId) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

//        EntityTransaction transaction = null;
//        try {
//            transaction = beginTransaction();
        CatalogUser user;
        try {
            user = em.createNamedQuery(CatalogUser.GET_USER_BY_ID_QUERY, CatalogUser.class).
                    setParameter("id", id).
                    getSingleResult();
        } catch (NoResultException ex) {
            user = null;
        }

        CatalogFile file;
        try {
            file = em.createNamedQuery(CatalogFile.GET_FILE_BY_ID_QUERY, CatalogFile.class).
                    setParameter("id", fileId).
                    getSingleResult();
        } catch (NoResultException ex) {
            file = null;
        }

        if (user == null) {
            throw new RejectedException("User not found!");
        }
        if (file == null) {
            throw new RejectedException("File not found!");
        }
        if (file.getAccessPermission() == AccessPermission.PRIVATE && !file.getOwner().equals(user)) {
            throw new RejectedException("Not allowed action!");
        }


        try {
            byte[] res = loadFile(file.getFilePath());
            return res;
        } catch (IOException ex) {
            throw new RejectedException("File not found!");
        }
//        } finally {
//            commitTransaction(transaction);
//        }
    }

    @Override
    public void deleteFile(int id,
            int fileId) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction();

            CatalogUser user;
            try {
                user = em.createNamedQuery(CatalogUser.GET_USER_BY_ID_QUERY, CatalogUser.class).
                        setParameter("id", id).
                        getSingleResult();
            } catch (NoResultException ex) {
                user = null;
            }

            CatalogFile file;
            try {
                file = em.createNamedQuery(CatalogFile.GET_FILE_BY_ID_QUERY, CatalogFile.class).
                        setParameter("id", fileId).
                        getSingleResult();
            } catch (NoResultException ex) {
                file = null;
            }

            if (user == null) {
                throw new RejectedException("User not found!");
            }
            if (file == null) {
                throw new RejectedException("File not found!");
            }
            if ((file.getAccessPermission() == AccessPermission.PRIVATE && !file.getOwner().equals(user))
                    || (file.getAccessPermission() == AccessPermission.PUBLIC && file.getWriteReadPermission() == WriteReadPermission.READ)) {
                throw new RejectedException("Not allowed action!");
            }

            em.remove(file);
        } finally {
            commitTransaction(transaction);
        }
    }

    @Override
    public void updateFile(int id,
            int fileId,
            byte[] actualFile) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

        EntityTransaction transaction = null;
        try {
            transaction = beginTransaction();

            CatalogUser user;
            try {
                user = em.createNamedQuery(CatalogUser.GET_USER_BY_ID_QUERY, CatalogUser.class).
                        setParameter("id", id).
                        getSingleResult();
            } catch (NoResultException ex) {
                user = null;
            }

            CatalogFile file;
            try {
                file = em.createNamedQuery(CatalogFile.GET_FILE_BY_ID_QUERY, CatalogFile.class).
                        setParameter("id", fileId).
                        getSingleResult();
            } catch (NoResultException ex) {
                file = null;
            }

            if (user == null) {
                throw new RejectedException("User not found!");
            }
            if (file == null) {
                throw new RejectedException("File not found!");
            }
            if ((file.getAccessPermission() == AccessPermission.PRIVATE && !file.getOwner().equals(user))
                    || (file.getAccessPermission() == AccessPermission.PUBLIC && file.getWriteReadPermission() == WriteReadPermission.READ)) {
                throw new RejectedException("Not allowed action!");
            }

            saveFile(actualFile, file.getFilePath());

            em.createNamedQuery(CatalogFile.UPDATE_FILE_QUERY).
                    setParameter("id", fileId).
                    setParameter("newTime", new Date(System.currentTimeMillis())).
                    executeUpdate();
        } catch (IOException ex) {
            throw new RejectedException("Bad file!");
        } finally {
            commitTransaction(transaction);
        }
    }

    @Override
    public List<CatalogFile> getAllFiles(int id) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

//        EntityTransaction transaction = null;
//        try {
//            transaction = beginTransaction();
        CatalogUser user;
        try {
            user = em.createNamedQuery(CatalogUser.GET_USER_BY_ID_QUERY, CatalogUser.class).
                    setParameter("id", id).
                    getSingleResult();
        } catch (NoResultException ex) {
            user = null;
        }

        if (user == null) {
            throw new RejectedException("User not found!");
        }

        List<CatalogFile> allFiles = em.createNamedQuery(CatalogFile.GET_AVAILABLE_FILES_QUERY, CatalogFile.class).
                setParameter("owner", user).
                getResultList();

        return allFiles;
//        } finally {
//            commitTransaction(transaction);
//        }
    }

    @Override
    public List<CatalogFile> getMyFiles(int id) throws RejectedException, RemoteException {
        if (!loggedInUsers.contains(id)) {
            throw new RejectedException("Not logged in!");
        }

//        EntityTransaction transaction = null;
//        try {
//            transaction = beginTransaction();
        CatalogUser user;
        try {
            user = em.createNamedQuery(CatalogUser.GET_USER_BY_ID_QUERY, CatalogUser.class).
                    setParameter("id", id).
                    getSingleResult();
        } catch (NoResultException ex) {
            user = null;
        }

        if (user == null) {
            throw new RejectedException("User not found!");
        }

        List<CatalogFile> userFiles = em.createNamedQuery(CatalogFile.GET_USER_FILES_QUERY, CatalogFile.class).
                setParameter("owner", user).
                getResultList();

        return userFiles;
//        } finally {
//            commitTransaction(transaction);
//        }
    }

    private EntityTransaction beginTransaction() {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        return transaction;
    }

    private void commitTransaction(EntityTransaction transaction) {
        transaction.commit();
    }

    private void saveFile(byte[] file, String filePath) throws FileNotFoundException, IOException {
        InputStream in = new ByteArrayInputStream(file);
        OutputStream out = new FileOutputStream(new File(filePath));
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private byte[] loadFile(String filePath) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(filePath);
        OutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        byte[] res = ((ByteArrayOutputStream) out).toByteArray();
        in.close();
        out.close();

        return res;
    }
}
