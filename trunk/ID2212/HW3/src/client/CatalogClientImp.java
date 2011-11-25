/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;
import server.Catalog;
import ui.CatalogResponsiveUI;
import utils.AlreadyLoggedInException;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public class CatalogClientImp implements CatalogClient {
    private static String host = "localhost";
    private static int port = 1099;
    private static String catalogName = "Catalog";
    private int currentUserId;
    private CatalogResponsiveUI ui;
    private Catalog catalog;

    public CatalogClientImp(CatalogResponsiveUI ui, String[] args) {
        this.ui = ui;

        if (args.length > 0) {
            host = args[0];
        }
    }

    @Override
    public List<CatalogFile> getAllFiles() {
        if (catalog == null) {
            return null;
        }

        List<CatalogFile> allFiles = null;
        try {
            allFiles = catalog.getAllFiles(this.currentUserId);
            ui.updateAllFiles(allFiles);
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }

        return allFiles;
    }

    @Override
    public List<CatalogFile> getMyFiles() {
        if (catalog == null) {
            return null;
        }

        List<CatalogFile> myFiles = null;
        try {
            myFiles = catalog.getMyFiles(this.currentUserId);
            ui.updateMyFiles(myFiles);
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }

        return myFiles;
    }

    @Override
    public byte[] downloadFile(CatalogFile selectedFile) {
        if (catalog == null) {
            return null;
        }

        if (selectedFile == null) {
            return null;
        }

        byte[] downloadedFile = null;

        try {
            downloadedFile = catalog.downloadFile(this.currentUserId, selectedFile.getId());
            ui.saveFile(downloadedFile);
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }

        return downloadedFile;
    }

    @Override
    public void updateFile(CatalogFile selectedFile, byte[] file) {
        if (catalog == null) {
            return;
        }

        try {
            catalog.updateFile(this.currentUserId, selectedFile.getId(), file);
            getAllFiles();
            getMyFiles();
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }
    }

    @Override
    public void uploadFile(String fileName, AccessPermission accessPerm, WriteReadPermission writeReadPerm, byte[] file) {
        if (catalog == null) {
            return;
        }

        try {
            catalog.uploadFile(this.currentUserId, fileName, accessPerm, writeReadPerm, file);
            getMyFiles();
            getAllFiles();
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }
    }

    @Override
    public void deleteFile(CatalogFile selectedFile) {
        if (catalog == null) {
            return;
        }

        if (selectedFile == null) {
            return;
        }

        try {
            catalog.deleteFile(this.currentUserId, selectedFile.getId());
            getAllFiles();
            getMyFiles();
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }
    }

    @Override
    public void register(String userName, String pwd) {
        try {
            catalog = (Catalog) Naming.lookup("//" + host + ":" + port + "/" + catalogName);
            catalog.registerUser(userName, pwd);
        } catch (NotBoundException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        } catch (MalformedURLException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }
    }

    @Override
    public void unregister() {
        if (catalog == null) {
            return;
        }

        try {
            //catalog.logout(this.currentUserId);
            ui.updateAllFiles(new ArrayList<CatalogFile>());
            ui.updateMyFiles(new ArrayList<CatalogFile>());
            ui.updateAfterLogout();
            catalog.unregisterUser(currentUserId);
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }
    }

    @Override
    public void login(String userName, String pwd) {
        try {
            catalog = (Catalog) Naming.lookup("//" + host + ":" + port + "/" + catalogName);
            this.currentUserId = catalog.login(userName, pwd);
            getMyFiles();
            getAllFiles();
            ui.updateAfterLogin(userName);
        } catch (NotBoundException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        } catch (MalformedURLException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        } catch (AlreadyLoggedInException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
            this.currentUserId = ex.getId();
            getMyFiles();
            getAllFiles();
            ui.updateAfterLogin(userName);
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }
    }

    @Override
    public void logout() {
        if (catalog == null) {
            return;
        }

        try {
            catalog.logout(this.currentUserId);
            ui.updateAfterLogout();
        } catch (RejectedException ex) {
            ui.showRejectedExceptionNotification(ex.getMessage());
        } catch (RemoteException ex) {
            ui.showRemoteExceptionNotification(ex.getMessage());
        }
    }
}
