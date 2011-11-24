/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.rmi.Remote;
import java.util.List;
import model.CatalogFile;
import model.CatalogUser;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public interface FileCatalog extends Remote {
    void registerUser(String name, String pwd) throws RejectedException;
    void unRegisterUser(String name, String pwd);
    CatalogUser login(String name, String pwd) throws RejectedException;
    void logout( String name);
    // TODO. Add parameters 
    void uploadFile(String userName, CatalogFile fileDescription, File file);
    File downloadFile(long fileId, String userName) throws RejectedException;
    void deleteFile(long fileId, String userName) throws RejectedException;
    void updateFile(long fileId, File file, String userName) throws RejectedException;
    
    List<CatalogFile> getAllFiles();
    List<CatalogFile> getMyFiles(String userName);
}
