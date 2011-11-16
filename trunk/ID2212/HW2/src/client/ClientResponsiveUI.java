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
    void updateAllItems(final List<MarketItem> items);
    void alertWishItem(final MarketItem item);
    void updatePostedItemSold(final MarketItem item);
    void showBuyConfirmationDialog();
    void showItemSoldNotificationMessage(final MarketItem item);
    void updateBalance(final float balance);
    
}
