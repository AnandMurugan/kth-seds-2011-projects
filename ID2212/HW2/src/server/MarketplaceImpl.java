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
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + name + "\" is already registered.");
        } else {
            clients.put(name, new ClientInfo(callback, account));
        }
    }

    @Override
    public synchronized void unregisterClient(String name) throws RemoteException, RejectedException {
        if (!clients.containsKey(name)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + name + "\" is not registered.");
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
    public synchronized void addItem(MarketItem item) throws RemoteException, RejectedException {
        String owner = item.getOwner();
        if (!clients.containsKey(owner)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + owner + "\" is not registered.");
        } else {
            items.add(item);
        }
    }

    @Override
    public synchronized void addWish(MarketItem wish) throws RemoteException, RejectedException {
        String owner = wish.getOwner();
        if (!clients.containsKey(owner)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + owner + "\" is not registered.");
        } else {
            wishes.add(wish);
        }
    }

    @Override
    public List<MarketItem> getItems() throws RemoteException {
        return items;
    }

    @Override
    public synchronized void buyItem(String name, MarketItem item) throws RemoteException, RejectedException {
        if (!clients.containsKey(name)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + name + "\" is not registered.");
        } else {
            if (!items.contains(item)) {
                throw new RejectedException("MARKETPLACE(" + marketName + "): "
                        + "Required item(" + item.getName() + ", " + item.getPrice() + ", " + item.getOwner() + ") doesn't exist.");
            } else {
                //Get info
                Account buyerAccount = clients.get(name).account;

                ClientInfo sellerInfo = clients.get(item.getOwner());
                Account sellerAccount = sellerInfo.account;
                MarketplaceCallbackable sellerCallback = sellerInfo.callback;

                float price = item.getPrice();

                //Remove item from the market 
                items.remove(item);

                //Withdraw money from the buyer
                buyerAccount.withdraw(price);

                //Deposit money to the seller
                sellerAccount.deposit(price);

                //Set new owner of the item
                item.setOwner(name);

                //Notify seller
                sellerCallback.notifyPurchaseSuccessful(item);
            }
        }
    }

    @Override
    public void removeWish(MarketItem wish) throws RemoteException, RejectedException {
        String owner = wish.getOwner();
        if (!clients.containsKey(owner)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + owner + "\" is not registered.");
        }
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
