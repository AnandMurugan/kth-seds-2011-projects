/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import server.MarketItem;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author julio
 */
public interface RemoteMarket extends Remote {
    public IMarketItem addItemToMarket(String itemName, Integer itemPrice,  RemoteClient seller) throws RemoteException;
    public IMarketItem addItemToWishList(String itemName, Integer itemPrice, RemoteClient buyer) throws RemoteException;
    public List<IMarketItem> getAvailableItems() throws RemoteException;
    // TODO. analize how to match the exact item to buy. Maybe by using a market id?
    public void buyItem(MarketItem item, RemoteClient buyer) throws RemoteException; // Should throw rejected reason also??
    
    /* Cases to consider:
     * What if more than 1 buyer wish the same item? what if one of them buys before the other...
     *
     * 
     */
    
}
