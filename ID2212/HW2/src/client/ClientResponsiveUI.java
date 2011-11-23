/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.List;
import market.MarketItem;

/**
 *
 * @author julio
 */
public interface ClientResponsiveUI {
    void updateAllItems(List<MarketItem> items);

    void alertWishItem(MarketItem item);

    void updateOnPostedItemSold(MarketItem item);

    void updateOnItemBought(MarketItem item);

    void updateOnPostItem(MarketItem item);

    void updateOnPostWish(MarketItem wish);

    void updateOnUnpostItem(MarketItem item);

    void updateOnUnpostWish(MarketItem wish);

    void showBuyConfirmationDialog();

    void showItemSoldNotificationMessage(MarketItem item);

    void updateBalance(float balance);

    void showBuyWishItemDialog(MarketItem item);

    void updateTitle(String msg);

    void showRejectedNotificationMessage(String message);

    void showExceptionNotificationMessage(String message);
}
