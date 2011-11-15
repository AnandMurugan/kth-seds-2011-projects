/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author julio
 */
public interface Marketplace extends Remote {
    public MarketItem addItemToMarket(String itemName, Integer itemPrice, MarketplaceCallbackable seller) throws RemoteException;

    public MarketItem addItemToWishList(String itemName, Integer itemPrice, MarketplaceCallbackable buyer) throws RemoteException;

    public List<MarketItem> getAvailableItems() throws RemoteException;
    // TODO. analize how to match the exact item to buy. Maybe by using a market id?

    public void buyItem(MarketItemImpl item, MarketplaceCallbackable buyer) throws RemoteException; // Should throw rejected reason also??
    /* Cases to consider:
     * What if more than 1 buyer wish the same item? what if one of them buys before the other...
     *
     * 
     */
}
