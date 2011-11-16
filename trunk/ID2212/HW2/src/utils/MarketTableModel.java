/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import server.MarketItem;

/**
 *
 * @author julio
 */
public class MarketTableModel extends AbstractTableModel {
    List<MarketItem> marketItemList;
    String headerList[] = new String[]{
        "Item", "Price"
    };

    public MarketTableModel(List<MarketItem> list) {
        marketItemList = list;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return marketItemList.size();
    }

    // this method is called to set the value of each cell
    @Override
    public Object getValueAt(int row, int column) {
        MarketItem item = null;
        item = marketItemList.get(row);

        switch (column) {
            case 0:
                return item.getName();
            case 1:
                return item.getPrice();
            default:
                return "";
        }
    }

    //This method will be used to display the name of columns
    public String getColumnName(int col) {
        return headerList[col];
    }

    public void addMarketItem(MarketItem item) {
        marketItemList.add(item);
        fireTableChanged(null);
    }

    public void removeMarketItem(int row) {
        if (row > -1) {
            marketItemList.remove(marketItemList.get(row));
            fireTableChanged(null);
        }
    }

    public void removeMarketItem(long id) {
        MarketItem tmp = null;
        for (MarketItem i : marketItemList) {
            if (i.getMarketItemId() == id) {
                tmp = i;
                break;
            }
        }

        if (tmp != null) {
            marketItemList.remove(tmp);
            fireTableChanged(null);
        }
    }
}