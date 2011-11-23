/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.AccessPermission;
import model.CatalogFile;

/**
 *
 * @author julio
 */
public class FileTableModel extends AbstractTableModel {
    List<CatalogFile> catalogFileList;
    String headerList[] = new String[]{
        "Name", "Size", "Owner", "Access"
    };

    public FileTableModel(List<CatalogFile> list) {
        catalogFileList = list;
    }

    @Override
    public int getColumnCount() {
        return 4;
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
                return item.getName();
            case 1:
                return item.getSize();
            case 2:
                return item.getOwner();
            case 3:
                if (item.getAccessPermission() == AccessPermission.PUBLIC)
                    return "Public";
                else
                    return "Private";
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

    public void removeMarketItem(long id) {
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