/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public interface Catalog extends Remote {
    void registerUser(String name, String password) throws RejectedException, RemoteException;

    void unregisterUser(int id) throws RejectedException, RemoteException;

    int login(String name, String password) throws RejectedException, RemoteException;

    void logout(int id) throws RejectedException, RemoteException;

    void uploadFile(int id, String name, AccessPermission access, WriteReadPermission writeRead, byte[] file) throws RejectedException, RemoteException;

    byte[] downloadFile(int id, int fileId) throws RejectedException, RemoteException;

    void deleteFile(int id, int fileId) throws RejectedException, RemoteException;

    void updateFile(int id, int fileId, byte[] file) throws RejectedException, RemoteException;

    List<CatalogFile> getAllFiles(int id) throws RejectedException, RemoteException;

    List<CatalogFile> getMyFiles(int id) throws RejectedException, RemoteException;
}
