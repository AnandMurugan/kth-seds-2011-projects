/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FileCatalogClientUI.java
 *
 * Created on Nov 23, 2011, 1:26:31 PM
 */
package ui;

import client.CatalogClient;
import client.CatalogClientImp;
import java.awt.Component;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;

/**
 *
 * @author julio
 */
public class FileCatalogClientUI extends javax.swing.JFrame implements FileCatResponsiveUI {
    private FileTableModel allFilesModel;
    private FileTableModel myFilesModel;
    private CatalogClient client;

    /** Creates new form FileCatalogClientUI */
    public FileCatalogClientUI() {
        initComponents();
        client = new CatalogClientImp(this);

        allFilesModel = new FileTableModel(new ArrayList<CatalogFile>());
        allFilesTable.setModel(allFilesModel);

        myFilesModel = new FileTableModel(new ArrayList<CatalogFile>());
        myFilesTable.setModel(myFilesModel);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        myFilesTable = new javax.swing.JTable();
        myDownloadBtn = new javax.swing.JButton();
        myUpdateBtn = new javax.swing.JButton();
        myDeleteBtn = new javax.swing.JButton();
        myUploadBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        allFilesTable = new javax.swing.JTable();
        downloadAllBtn = new javax.swing.JButton();
        catUpdateBtn = new javax.swing.JButton();
        catDeleteBtn = new javax.swing.JButton();
        refreshAllBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        userNameLbl = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        loginMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        myFilesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(myFilesTable);

        myDownloadBtn.setText("Download");
        myDownloadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myDownloadBtnActionPerformed(evt);
            }
        });

        myUpdateBtn.setText("Update");

        myDeleteBtn.setText("Delete");

        myUploadBtn.setText("Upload");
        myUploadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myUploadBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(myDownloadBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myUpdateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myDeleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(myUploadBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myUploadBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myDownloadBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myUpdateBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myDeleteBtn)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("My Files", jPanel2);

        jPanel1.setPreferredSize(new java.awt.Dimension(559, 300));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 300));

        allFilesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(allFilesTable);

        downloadAllBtn.setText("Download");
        downloadAllBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadAllBtnActionPerformed(evt);
            }
        });

        catUpdateBtn.setText("Update");

        catDeleteBtn.setText("Delete");

        refreshAllBtn.setText("Refresh List");
        refreshAllBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAllBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refreshAllBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(catUpdateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                        .addComponent(catDeleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                        .addComponent(downloadAllBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)))
                .addGap(56, 56, 56))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(refreshAllBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downloadAllBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catUpdateBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(catDeleteBtn)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Catalog Files", jPanel1);

        jLabel1.setText("User:");

        jMenu1.setText("Catalog");

        loginMenuItem.setText("Login...");
        loginMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(loginMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(userNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(userNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void refreshAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAllBtnActionPerformed
    Runnable getFilesTask = new Runnable() {
        @Override
        public void run() {
            client.getAllFiles();
        }
    };
    (new Thread(getFilesTask)).start();
}//GEN-LAST:event_refreshAllBtnActionPerformed

private void downloadAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadAllBtnActionPerformed
    Runnable getFilesTask = new Runnable() {
        @Override
        public void run() {
            client.downloadFile(null);
        }
    };
    (new Thread(getFilesTask)).start();
}//GEN-LAST:event_downloadAllBtnActionPerformed

private void loginMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginMenuItemActionPerformed
    (new ConnectDialog(this, true)).setVisible(true);
}//GEN-LAST:event_loginMenuItemActionPerformed

private void myUploadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myUploadBtnActionPerformed
    (new UploadFileDialog(this, true)).setVisible(true);
}//GEN-LAST:event_myUploadBtnActionPerformed

private void myDownloadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myDownloadBtnActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_myDownloadBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileCatalogClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileCatalogClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileCatalogClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileCatalogClientUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileCatalogClientUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable allFilesTable;
    private javax.swing.JButton catDeleteBtn;
    private javax.swing.JButton catUpdateBtn;
    private javax.swing.JButton downloadAllBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JMenuItem loginMenuItem;
    private javax.swing.JButton myDeleteBtn;
    private javax.swing.JButton myDownloadBtn;
    private javax.swing.JTable myFilesTable;
    private javax.swing.JButton myUpdateBtn;
    private javax.swing.JButton myUploadBtn;
    private javax.swing.JButton refreshAllBtn;
    private javax.swing.JLabel userNameLbl;
    // End of variables declaration//GEN-END:variables

    @Override
    public void updateAllFiles(final List<CatalogFile> files) {
        Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                allFilesModel.setCatalogFileList(files);
            }
        };
        try {
            SwingUtilities.invokeAndWait(updateUI);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateMyFiles(final List<CatalogFile> files) {
        Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                myFilesModel.setCatalogFileList(files);
            }
        };
        try {
            SwingUtilities.invokeAndWait(updateUI);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteFile(final CatalogFile file) {
        Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                allFilesModel.removeCatalogFile(file.getId());
            }
        };
        try {
            SwingUtilities.invokeAndWait(updateUI);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void uploadFile(final String fileName, final AccessPermission accessPerm, final WriteReadPermission writeReadPerm, final File file) {
        Runnable uploadFileTask = new Runnable() {
            @Override
            public void run() {
                if (accessPerm == AccessPermission.PRIVATE){
                client.uploadFile(fileName, accessPerm, null, file);
                } else if (accessPerm == AccessPermission.PUBLIC){
                    client.uploadFile(fileName, accessPerm, writeReadPerm, file);
                }
            }
        };
        
        (new Thread(uploadFileTask)).start();
    }

    @Override
    public void deleteMyFile(final CatalogFile file) {
        Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                myFilesModel.removeCatalogFile(file.getId());
            }
        };
        try {
            SwingUtilities.invokeAndWait(updateUI);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void login(final String name, final String pwd) {
        Runnable loginTask = new Runnable() {
            @Override
            public void run() {
                client.login(name, pwd);
            }
        };

        (new Thread(loginTask)).start();
    }

    @Override
    public void saveFile(File file) {
        JFileChooser saveFileDlg = new JFileChooser();
        int retrieval = saveFileDlg.showSaveDialog((Component) this);

        if (retrieval == JFileChooser.APPROVE_OPTION) {
            String fileName = saveFileDlg.getSelectedFile().getPath();
            // TODO. Write file
        }
    }

    @Override
    public void setUserName(final String name) {
        Runnable updateUI = new Runnable() {
            @Override
            public void run() {
                userNameLbl.setText(name);
            }
        };
        try {
            SwingUtilities.invokeAndWait(updateUI);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FileCatalogClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
