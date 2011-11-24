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

/**
 *
 * @author julio
 */
public interface FileCatalog extends Remote {
    void registerUser(String name, String pwd);
    void unRegisterUser(String name, String pwd);
    CatalogUser login(String name, String pwd);
    void logout( String name, String pwd);
    // TODO. Add parameters 
    void uploadFile(String userName, CatalogFile fileDescription, File file);
    File downloadFile(long fileId, String userName);
    void deleteFile(long fileId, String userName);
    void updateFile(long fileId, File file, String userName);
    
    List<CatalogFile> getAllFiles();
    List<CatalogFile> getMyFiles(String userName);
}
