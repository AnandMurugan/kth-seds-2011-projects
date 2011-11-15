/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author julio
 */
public class MarketplaceImpl extends UnicastRemoteObject implements Marketpalce {
    private List<MarketItemImpl> wishList;
    private Map<Integer, Entry<MarketItemImpl, MarketplaceCallbackable>> marketItems;
    private String marketName;
    private Integer currentItemId = 0;

    public MarketplaceImpl(String marketName) throws RemoteException {
        super();
        this.marketName = marketName;
    }

    @Override
    public MarketItem addItemToMarket(String itemName, Integer itemPrice, MarketplaceCallbackable seller) throws RemoteException {
        currentItemId++;
        MarketItemImpl item = new MarketItemImpl(currentItemId, itemName, itemPrice);
        marketItems.put(item.getMarketId(), new SimpleEntry(item, seller));
        return item;
    }

    @Override
    public MarketItem addItemToWishList(String itemName, Integer itemPrice, MarketplaceCallbackable buyer) throws RemoteException {
        currentItemId++;
        MarketItemImpl item = new MarketItemImpl(currentItemId, itemName, itemPrice);
        marketItems.put(item.getMarketId(), new SimpleEntry(item, buyer));
        return item;
    }

    @Override
    public List<MarketItem> getAvailableItems() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void buyItem(MarketItemImpl item, MarketplaceCallbackable buyer) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
