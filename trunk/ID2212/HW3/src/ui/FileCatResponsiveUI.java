/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.File;
import java.util.List;
import model.CatalogFile;

/**
 *
 * @author julio
 */
public interface FileCatResponsiveUI {
    void updateAllFiles(List<CatalogFile> files);
    void updateMyFiles(List<CatalogFile> files);
    void deleteMyFile(CatalogFile file);
    void deleteFile(CatalogFile file);
    void uploadFile(CatalogFile file);
    void saveFile(File file);
    void login(String name, String pwd); // called by the login dialog
    void setUserName(String name);
}
