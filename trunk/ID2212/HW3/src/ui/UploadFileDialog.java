/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * UploadFileDialog.java
 *
 * Created on Nov 24, 2011, 11:47:32 AM
 */
package ui;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import model.AccessPermission;
import model.WriteReadPermission;

/**
 *
 * @author julio
 */
public class UploadFileDialog extends javax.swing.JDialog {
    AccessPermission access = AccessPermission.PRIVATE;
    WriteReadPermission writeRead = WriteReadPermission.READ;

    /** Creates new form UploadFileDialog */
    public UploadFileDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        accessPermGrp = new javax.swing.ButtonGroup();
        readWriteGrp = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        publicRdbtn = new javax.swing.JRadioButton();
        privateRdbtn = new javax.swing.JRadioButton();
        readRdbtn = new javax.swing.JRadioButton();
        writeRdbtn = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        filePathTxt = new javax.swing.JTextField();
        browseBtn = new javax.swing.JButton();
        fileNameTxt = new javax.swing.JTextField();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Upload File");

        jLabel1.setText("File Name:");

        accessPermGrp.add(publicRdbtn);
        publicRdbtn.setLabel("Public");
        publicRdbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publicRdbtnActionPerformed(evt);
            }
        });

        accessPermGrp.add(privateRdbtn);
        privateRdbtn.setSelected(true);
        privateRdbtn.setLabel("Private");
        privateRdbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                privateRdbtnActionPerformed(evt);
            }
        });

        readWriteGrp.add(readRdbtn);
        readRdbtn.setEnabled(false);
        readRdbtn.setLabel("Read");
        readRdbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                readRdbtnActionPerformed(evt);
            }
        });

        readWriteGrp.add(writeRdbtn);
        writeRdbtn.setEnabled(false);
        writeRdbtn.setLabel("Write");
        writeRdbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeRdbtnActionPerformed(evt);
            }
        });

        jLabel3.setText("Access Permission:");

        jLabel4.setText("Read-Write Permission:");

        jLabel5.setText("File Path:");

        browseBtn.setText("...");
        browseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseBtnActionPerformed(evt);
            }
        });

        okBtn.setText("Ok");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fileNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(publicRdbtn, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(readRdbtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(privateRdbtn)
                            .addComponent(writeRdbtn))
                        .addGap(2, 2, 2))
                    .addComponent(jLabel2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(okBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelBtn))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(filePathTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseBtn)))
                        .addGap(12, 12, 12)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fileNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(publicRdbtn)
                                .addComponent(privateRdbtn))
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(readRdbtn)
                            .addComponent(writeRdbtn))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filePathTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(browseBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn)
                    .addComponent(okBtn))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void browseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseBtnActionPerformed
    JFileChooser browseDlg = new JFileChooser();
    int result = browseDlg.showOpenDialog((Component) this);

    if (result == JFileChooser.APPROVE_OPTION) {
        filePathTxt.setText(browseDlg.getSelectedFile().getPath());
    }

}//GEN-LAST:event_browseBtnActionPerformed

private void publicRdbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publicRdbtnActionPerformed
    if (publicRdbtn.isSelected()) {
        readRdbtn.setEnabled(true);
        writeRdbtn.setEnabled(true);
        access = AccessPermission.PUBLIC;
        readRdbtn.setSelected(true);
    }
}//GEN-LAST:event_publicRdbtnActionPerformed

private void privateRdbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_privateRdbtnActionPerformed
    if (privateRdbtn.isSelected()) {
        readRdbtn.setSelected(false);
        writeRdbtn.setSelected(false);
        readRdbtn.setEnabled(false);
        writeRdbtn.setEnabled(false);
        access = AccessPermission.PRIVATE;
    }
}//GEN-LAST:event_privateRdbtnActionPerformed

private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
    // Validate UI data
    if (!fileNameTxt.getText().isEmpty() && !filePathTxt.getText().isEmpty()) {
        File file = new File(filePathTxt.getText());
        
        if (file.exists()) {
            ((FileCatResponsiveUI) getParent()).uploadFile(fileNameTxt.getText(), access, writeRead, file);
        }
    }
    
    dispose();
}//GEN-LAST:event_okBtnActionPerformed

private void readRdbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_readRdbtnActionPerformed
    writeRead = WriteReadPermission.READ;
}//GEN-LAST:event_readRdbtnActionPerformed

private void writeRdbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeRdbtnActionPerformed
    writeRead = WriteReadPermission.WRITE;
}//GEN-LAST:event_writeRdbtnActionPerformed

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
            java.util.logging.Logger.getLogger(UploadFileDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UploadFileDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UploadFileDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UploadFileDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UploadFileDialog dialog = new UploadFileDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup accessPermGrp;
    private javax.swing.JButton browseBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JTextField fileNameTxt;
    private javax.swing.JTextField filePathTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton okBtn;
    private javax.swing.JRadioButton privateRdbtn;
    private javax.swing.JRadioButton publicRdbtn;
    private javax.swing.JRadioButton readRdbtn;
    private javax.swing.ButtonGroup readWriteGrp;
    private javax.swing.JRadioButton writeRdbtn;
    // End of variables declaration//GEN-END:variables
}
