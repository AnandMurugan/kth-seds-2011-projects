/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import model.AccessPermission;
import model.CatalogFile;
import model.CatalogUser;
import server.FileCatalog;
import ui.FileCatResponsiveUI;

/**
 *
 * @author julio
 */
public class CatalogClientImp implements CatalogClient {
    private CatalogUser currentUser;
    FileCatResponsiveUI ui;
    FileCatalog catalog;
    
    public CatalogClientImp(FileCatResponsiveUI ui){
        this.ui = ui;
    }
    
    @Override
    public void getAllFiles() {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getMyFiles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void downloadFile(CatalogFile selectedFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateFile(CatalogFile selectedFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CatalogFile uploadFile(String name, AccessPermission access) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteFile(CatalogFile selectedFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void register(String userName, String pwd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unregister() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void login(String userName, String pwd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
