/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.AccessPermission;
import model.CatalogFile;
import model.WriteReadPermission;

/**
 *
 * @author julio
 */
public class CatalogFileTableModel extends AbstractTableModel {
    List<CatalogFile> catalogFileList;
    String headerList[] = new String[]{
        "Name", "Size", "Owner", "Access", "WRITE/READ", "Modified"
    };

    public CatalogFileTableModel(List<CatalogFile> list) {
        catalogFileList = list;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public int getRowCount() {
        return catalogFileList.size();
    }

    // this method is called to set the value of each cell
    @Override
    public Object getValueAt(int row, int column) {
        CatalogFile item = null;
        item = catalogFileList.get(row);

        switch (column) {
            case 0:
                return item.getFileName();
            case 1:
                return item.getFileSize();
            case 2:
                return item.getOwner().getName();
            case 3:
                return item.getAccessPermission();
            case 4:
                WriteReadPermission p = item.getWriteReadPermission();
                return p != null ? p : "-";
            case 5:
                return item.getLastModifiedTime();
            default:
                return "";
        }
    }

    //This method will be used to display the name of columns
    @Override
    public String getColumnName(int col) {
        return headerList[col];
    }

    public void setCatalogFileList(List<CatalogFile> list) {
        catalogFileList = list;
        fireTableChanged(null);
    }

    public void addCatalogFile(CatalogFile item) {
        catalogFileList.add(item);
        fireTableChanged(null);
    }

    public CatalogFile getCatalogFile(int row) {
        if (row > -1) {
            return catalogFileList.get(row);
        }
        return null;
    }

    public void removeCatalogFile(int row) {
        if (row > -1) {
            catalogFileList.remove(catalogFileList.get(row));
            fireTableChanged(null);
        }
    }

    public void removeCatalogFile(long id) {
        CatalogFile tmp = null;
        for (CatalogFile i : catalogFileList) {
            if (i.getId() == id) {
                tmp = i;
                break;
            }
        }

        if (tmp != null) {
            catalogFileList.remove(tmp);
            fireTableChanged(null);
        }
    }
}