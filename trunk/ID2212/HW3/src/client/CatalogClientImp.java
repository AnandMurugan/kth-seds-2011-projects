/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.AccessPermission;
import model.CatalogFile;
import model.CatalogUser;
import model.WriteReadPermission;
import server.FileCatalog;
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
    private boolean isLoggedIn;
    private FileCatResponsiveUI ui;
    private FileCatalog catalog;
    List<CatalogFile> testList = new ArrayList<CatalogFile>();

    public CatalogClientImp(FileCatResponsiveUI ui) {
        this.ui = ui;

        // TODO. Remove test data
        /****Test data***/
        int fileSize = 100;
        CatalogUser testUser = new CatalogUser(1, "testUser", 10);
        testList.add(new CatalogFile(1, "file1", fileSize, testUser, AccessPermission.PUBLIC, null, WriteReadPermission.WRITE, null));
        testList.add(new CatalogFile(2, "file2", fileSize, testUser, AccessPermission.PUBLIC, null, WriteReadPermission.WRITE, null));
        testList.add(new CatalogFile(3, "file3", fileSize, testUser, AccessPermission.PUBLIC, null, WriteReadPermission.WRITE, null));
        testList.add(new CatalogFile(4, "file4", fileSize, testUser, AccessPermission.PUBLIC, null, WriteReadPermission.WRITE, null));
        testList.add(new CatalogFile(5, "file5", fileSize, testUser, AccessPermission.PUBLIC, null, WriteReadPermission.WRITE, null));
        testList.add(new CatalogFile(6, "file6", fileSize, testUser, AccessPermission.PUBLIC, null, WriteReadPermission.WRITE, null));
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
        try {
            downloadedFile = catalog.downloadFile(selectedFile.getId(), this.currentUser);
            //ui.selectDestination();
        } catch(RejectedException ex){
            
        }
        
        return downloadedFile;
    }

    @Override
    public void updateFile(CatalogFile selectedFile, File file) {
        catalog.uploadFile(this.currentUser, selectedFile, file);
    }

    @Override
    public void uploadFile(CatalogFile fileDesc, File file) {
        catalog.uploadFile(this.currentUser, fileDesc, file);
    }

    @Override
    public void deleteFile(CatalogFile selectedFile) {
        try {
            catalog.deleteFile(selectedFile.getId(), this.currentUser);
        } catch (RejectedException ex) {
        }

    }

    @Override
    public void register(String userName, String pwd) {
        try {
            catalog.registerUser(userName, pwd);
            // ui.notifyRegistrationSuccessful(userName, pwd);
        } catch (RejectedException ex) {
        }
    }

    @Override
    public void unregister(String pwd) {
        catalog.unRegisterUser(currentUser, pwd);
    }

    @Override
    public void login(String userName, String pwd) {
        try {
            catalog.login(userName, pwd);
        } catch (RejectedException ex) {
        }
    }

    @Override
    public void logout() {
        catalog.logout(this.currentUser);
    }
}
