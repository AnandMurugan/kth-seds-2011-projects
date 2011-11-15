/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import common.IMarketItem;
import common.RemoteClient;
import common.RemoteMarket;
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
public class MarketImpl extends UnicastRemoteObject implements RemoteMarket {
    private List<MarketItem> wishList;
    private Map<Integer, Entry<MarketItem, RemoteClient>> marketItems;
    private String marketName;
    private Integer currentItemId = 0;

    public MarketImpl(String marketName) throws RemoteException {
        super();
        this.marketName = marketName;

    }

    @Override
    public IMarketItem addItemToMarket(String itemName, Integer itemPrice, RemoteClient seller) throws RemoteException {
        currentItemId++;
        MarketItem item = new MarketItem(currentItemId, itemName, itemPrice);
        marketItems.put(item.getMarketId(), new SimpleEntry(item, seller));
        return item;
    }

    @Override
    public IMarketItem addItemToWishList(String itemName, Integer itemPrice, RemoteClient buyer) throws RemoteException {
        currentItemId++;
        MarketItem item = new MarketItem(currentItemId, itemName, itemPrice);
        marketItems.put(item.getMarketId(), new SimpleEntry(item, buyer));
        return item;
    }

    @Override
    public List<IMarketItem> getAvailableItems() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void buyItem(MarketItem item, RemoteClient buyer) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
