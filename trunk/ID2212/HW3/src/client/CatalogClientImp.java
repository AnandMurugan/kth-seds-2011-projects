/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.CatalogFile;
import server.Catalog;
import ui.FileCatResponsiveUI;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public class CatalogClientImp implements CatalogClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;
    private static final String MARKETPLACE = "FileCat";
    private String currentUser;
    private int currentUserId;
    private boolean isLoggedIn;
    private FileCatResponsiveUI ui;
    private Catalog catalog;
    List<CatalogFile> testList = new ArrayList<CatalogFile>();

    public CatalogClientImp(FileCatResponsiveUI ui) {
        this.ui = ui;
    }

    @Override
    public List<CatalogFile> getAllFiles() {

        //ui.updateAllFiles(catalog.getAllFiles());
        ui.updateAllFiles(testList);
        return testList;
    }

    @Override
    public List<CatalogFile> getMyFiles() {
        //ui.updateAllFiles(catalog.getMyFiles());
        ui.updateMyFiles(testList);
        return testList;
    }

    @Override
    public File downloadFile(CatalogFile selectedFile) {
        File downloadedFile = null;
        ui.saveFile(downloadedFile);
        /*try {
        downloadedFile = catalog.saveFile(selectedFile.getId(), this.currentUser);
        ui.saveFile();
        } catch(RejectedException ex){
        
        }*/

        return downloadedFile;
    }

    @Override
    public void updateFile(CatalogFile selectedFile, File file) {
        try {
            catalog.updateFile(this.currentUserId, selectedFile.getId(), file);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void uploadFile(CatalogFile fileDesc, File file) {
        //catalog.uploadFile(this.currentUser, fileDesc, file);
    }

    @Override
    public void deleteFile(CatalogFile selectedFile) {
        try {
            catalog.deleteFile(this.currentUserId, selectedFile.getId());
        } catch (RejectedException ex) {
        } catch (RemoteException ex) {
        }

    }

    @Override
    public void register(String userName, String pwd) {
        try {

            catalog.registerUser(userName, pwd);
            // ui.notifyRegistrationSuccessful(userName, pwd);
        } catch (RejectedException ex) {
        } catch (RemoteException ex) {
        }
    }

    @Override
    public void unregister(String pwd) {
        try {
            catalog.unregisterUser(currentUserId);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void login(String userName, String pwd) {
        try {
            catalog.login(userName, pwd);
        } catch (RejectedException ex) {
        } catch (RemoteException ex) {
        }
    }

    @Override
    public void logout() {
        try {
            catalog.logout(this.currentUserId);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
