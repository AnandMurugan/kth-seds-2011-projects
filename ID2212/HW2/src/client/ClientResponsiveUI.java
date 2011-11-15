/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.List;
import server.MarketItem;

/**
 *
 * @author julio
 */
public interface ClientResponsiveUI {
    void updateAllItems(List<MarketItem> items);
    void updateWishItems(List<MarketItem> items);
    void updateAddedItems(MarketItem items);
    void showBuyConfirmationDialog();
    
    
}
