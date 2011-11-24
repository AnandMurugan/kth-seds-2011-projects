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
    void registerUser(String name, String pwd) throws RejectedException, RemoteException;

    void unregisterUser(int id) throws RejectedException, RemoteException;

    int login(String name, String pwd) throws RejectedException, RemoteException;

    void logout(int id) throws RejectedException, RemoteException;

    int uploadFile(int id, String name, AccessPermission access, WriteReadPermission writeRead, File file) throws RejectedException, RemoteException;

    File downloadFile(int id, int fileId) throws RejectedException, RemoteException;

    void deleteFile(int id, int fileId) throws RejectedException, RemoteException;

    void updateFile(int id, int fileId, File file) throws RejectedException, RemoteException;

    List<CatalogFile> getAllFiles(int id) throws RejectedException, RemoteException;

    List<CatalogFile> getMyFiles(String userName) throws RejectedException, RemoteException;
}
