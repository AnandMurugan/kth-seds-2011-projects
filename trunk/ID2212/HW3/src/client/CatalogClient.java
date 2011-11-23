/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import model.AccessPermission;
import model.CatalogFile;
import model.CatalogUser;

/**
 *
 * @author julio
 */
public interface CatalogClient {
    // TODO. Add parameters
    void getAllFiles();
    void getMyFiles();
    void downloadFile(CatalogFile selectedFile);
    void updateFile(CatalogFile selectedFile);
    CatalogFile uploadFile(String name, AccessPermission access); // server should create the CatalogFile object and return it
    void deleteFile(CatalogFile selectedFile);
    void register(String userName, String pwd); // should throw an exception when the user already exists
                                                // should validate the length of the pwd.
    void unregister();
    void login(String userName, String pwd); // Verify if the user is already logged from other client. 
}
