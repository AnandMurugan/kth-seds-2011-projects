/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;
import utils.RejectedException;

/**
 *
 * @author Igor
 */
public class CatalogImpl extends UnicastRemoteObject implements Catalog {
    private static final EntityManager em = Persistence.createEntityManagerFactory("model").createEntityManager();

    public CatalogImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerUser(String name, String pwd) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unregisterUser(int id) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int login(String name, String pwd) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void logout(int id) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int uploadFile(int id, String name, AccessPermission access, WriteReadPermission writeRead, File file) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File downloadFile(int id, int fileId) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteFile(int id, int fileId) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateFile(int id, int fileId, File file) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CatalogFile> getAllFiles(int id) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CatalogFile> getMyFiles(String userName) throws RejectedException, RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
