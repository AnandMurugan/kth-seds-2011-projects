/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;
import server.Catalog;
import ui.FileCatResponsiveUI;
import utils.AlreadyLoggedInException;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public class CatalogClientImp implements CatalogClient {
    private static final String DEFAULT_HOST = "130.229.129.151";
    private static final int DEFAULT_PORT = 1099;
    private static final String CATALOG = "Catalog";
    private String currentUser;
    private int currentUserId;
    private boolean isLoggedIn;
    private FileCatResponsiveUI ui;
    private Catalog catalog;

    public CatalogClientImp(FileCatResponsiveUI ui) {
        this.ui = ui;
    }

    @Override
    public List<CatalogFile> getAllFiles() {
        List<CatalogFile> allFiles = null;
        try {
            allFiles = catalog.getAllFiles(this.currentUserId);
            ui.updateAllFiles(allFiles);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return allFiles;
    }

    @Override
    public List<CatalogFile> getMyFiles() {
        List<CatalogFile> myFiles = null;
        try {
            myFiles = catalog.getMyFiles(this.currentUserId);
            ui.updateMyFiles(myFiles);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return myFiles;
    }

    @Override
    public byte[] downloadFile(CatalogFile selectedFile) {
        byte[] downloadedFile = null;

        try {
            downloadedFile = catalog.downloadFile(this.currentUserId, selectedFile.getId());
            ui.saveFile(downloadedFile);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return downloadedFile;
    }

    @Override
    public void updateFile(CatalogFile selectedFile, byte[] file) {
        try {
            catalog.updateFile(this.currentUserId, selectedFile.getId(), file);
            getAllFiles();
            getMyFiles();
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void uploadFile(String fileName, AccessPermission accessPerm, WriteReadPermission writeReadPerm, byte[] file) {
        try {
            catalog.uploadFile(this.currentUserId, fileName, accessPerm, writeReadPerm, file);
            getMyFiles();
            getAllFiles();
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteFile(CatalogFile selectedFile) {
        try {
            catalog.deleteFile(this.currentUserId, selectedFile.getId());
            getAllFiles();
            getMyFiles();
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void register(String userName, String pwd) {
        try {
            catalog = (Catalog) Naming.lookup("//" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + CATALOG);
            catalog.registerUser(userName, pwd);
        } catch (NotBoundException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
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
            catalog = (Catalog) Naming.lookup("//" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + CATALOG);
            this.currentUserId = catalog.login(userName, pwd);
            getMyFiles();
            getAllFiles();
            ui.updateAfterLogin(userName);
        } catch (NotBoundException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyLoggedInException ex) {
            this.currentUserId = ex.getId();
            getMyFiles();
            getAllFiles();
            ui.updateAfterLogin(userName);
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void logout() {
        try {
            catalog.logout(this.currentUserId);
            ui.updateAfterlogout();
        } catch (RejectedException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(CatalogClientImp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
