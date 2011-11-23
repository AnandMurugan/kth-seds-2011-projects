/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Remote;
import model.CatalogUser;

/**
 *
 * @author julio
 */
public interface FileCatalog extends Remote {
    void registerUser(String name, String pwd);
    void unRegisterUser(String name, String pwd);
    CatalogUser login(String name, String pwd);
    // TODO. Add parameters 
    void uploadFile();
    void downloadFile();
    void deleteFile();
    void updateFile();
            
    
}
