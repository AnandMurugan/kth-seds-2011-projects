/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.File;
import java.util.List;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;

/**
 *
 * @author julio
 */
public interface CatalogResponsiveUI {
    void updateAllFiles(List<CatalogFile> files);

    void updateMyFiles(List<CatalogFile> files);

    void deleteMyFile(CatalogFile file);

    void deleteFile(CatalogFile file);

    void uploadFile(String fileName, AccessPermission accessPerm, WriteReadPermission writeReadPerm, File file);

    void saveFile(byte[] file);

    void login(String name, String pwd); // called by the login dialog

    void setUserName(String name);

    void register(String name, String pwd);

    void updateAfterLogin(String userName);

    void updateAfterLogout();

    void showRemoteExceptionNotification(String msg);
    
    void showRejectedExceptionNotification(String msg);
}
