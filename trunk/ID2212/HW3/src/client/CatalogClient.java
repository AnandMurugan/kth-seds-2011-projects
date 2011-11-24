/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.List;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;

/**
 *
 * @author julio
 */
public interface CatalogClient {
    // TODO. Add parameters
    List<CatalogFile> getAllFiles();

    List<CatalogFile> getMyFiles();

    byte[] downloadFile(CatalogFile selectedFile);

    void updateFile(CatalogFile selectedFile, byte[] file);

    void uploadFile(String fileName, AccessPermission accessPerm, WriteReadPermission writeReadPerm, byte[] file);

    void deleteFile(CatalogFile selectedFile);

    void register(String userName, String pwd); // should throw an exception when the user already exists             // should validate the length of the pwd.

    void unregister();

    void login(String userName, String pwd); // Verify if the user is already logged from other client. 

    void logout();
}
