/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import bank.Account;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public class MarketplaceImpl extends UnicastRemoteObject implements Marketplace {
    private String marketName;
    private Map<String, ClientInfo> clients;
    private List<MarketItem> items;
    private List<MarketItem> wishes;

    public MarketplaceImpl(String marketName) throws RemoteException {
        super();
        this.marketName = marketName;
        this.clients = new HashMap<String, ClientInfo>();
        this.items = new LinkedList<MarketItem>();
        this.wishes = new LinkedList<MarketItem>();
    }

    @Override
    public synchronized void registerClient(String name, MarketplaceCallbackable callback, Account account) throws RemoteException, RejectedException {
        if (clients.containsKey(name)) {
            throw new RejectedException("Marketplace: A client with name \"" + name + "\" is already registered.");
        } else {
            clients.put(name, new ClientInfo(callback, account));
        }
    }

    @Override
    public synchronized void unregisterClient(String name) throws RemoteException, RejectedException {
        if (!clients.containsKey(name)) {
            throw new RejectedException("Marketplace: A client with name \"" + name + "\" is not registered.");
        } else {
            //SLOW IMPLEMENTATION (NETWORK CALLS)
            for (int i = 0; i < items.size(); i++) {
                MarketItem item = items.get(i);
                if (item.getOwner().equals(name)) {
                    items.remove(i);
                }
            }

            //SLOW IMPLEMENTATION (NETWORK CALLS)
            for (int i = 0; i < wishes.size(); i++) {
                MarketItem wish = wishes.get(i);
                if (wish.getOwner().equals(name)) {
                    wishes.remove(i);
                }
            }

            clients.remove(name);
        }
    }

    @Override
    public synchronized void addItem(MarketItem item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void addWish(MarketItem wish) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MarketItem> getItems() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void buyItem(String name, MarketItem item) throws RemoteException, RejectedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeWish(MarketItem wish) throws RemoteException, RejectedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class ClientInfo {
        public MarketplaceCallbackable callback;
        public Account account;

        public ClientInfo(MarketplaceCallbackable callback, Account account) {
            this.callback = callback;
            this.account = account;
        }
    }
}
