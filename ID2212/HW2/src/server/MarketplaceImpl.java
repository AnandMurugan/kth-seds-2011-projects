/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import bank.Account;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public class MarketplaceImpl extends UnicastRemoteObject implements Marketplace {
    private String marketName;
    private Map<String, ClientInfo> clients;
    private Map<Long, MarketItem> items;
    private Map<Long, MarketItem> wishes;
    private static long marketItemsIdCounter;

    public MarketplaceImpl(String marketName) throws RemoteException {
        super();
        this.marketName = marketName;
        this.clients = new HashMap<String, ClientInfo>();
        this.items = new HashMap<Long, MarketItem>();
        this.wishes = new HashMap<Long, MarketItem>();

        //Start wish handler
        Runnable wishHandlingTask = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(5000);

                        for (MarketItem wish : wishes.values()) {
                            String wishName = wish.getName();
                            float wishPrice = wish.getPrice();
                            String wishOwner = wish.getOwner();

                            for (MarketItem item : items.values()) {
                                String itemName = item.getName();
                                float itemPrice = item.getPrice();
                                String itemOwner = item.getOwner();

                                if ((wishName.equals(itemName)) && (wishPrice >= itemPrice) && (!wishOwner.equals(itemOwner))) {
                                    MarketplaceCallbackable wisher = clients.get(wishOwner).callback;
                                    wisher.notifyItemAvailable(item);
                                    break;
                                }
                            }
                        }
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(MarketplaceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(MarketplaceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        new Thread(wishHandlingTask, "Wish Handler").start();
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
            for (Entry<Long, MarketItem> entry : items.entrySet()) {
                MarketItem item = entry.getValue();
                Long id = entry.getKey();
                if (item.getOwner().equals(name)) {
                    items.remove(id);
                }
            }

            for (Entry<Long, MarketItem> entry : wishes.entrySet()) {
                MarketItem wish = entry.getValue();
                Long id = entry.getKey();
                if (wish.getOwner().equals(name)) {
                    wishes.remove(id);
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
            if (hasItem(item)) {
                throw new RejectedException("MARKETPLACE(" + marketName + "): This item is already available in the market.");
            } else {
                long id = marketItemsIdCounter++;
                item.setMarketItemId(id);
                items.put(id, item);
            }
        }
    }

    @Override
    public synchronized void addWish(MarketItem wish) throws RemoteException, RejectedException {
        String owner = wish.getOwner();
        if (!clients.containsKey(owner)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + owner + "\" is not registered.");
        } else {
            long id = marketItemsIdCounter++;
            wish.setMarketItemId(id);
            wishes.put(id, wish);
        }
    }

    @Override
    public Collection<MarketItem> getItems() throws RemoteException {
        return items.values();
    }

    @Override
    public synchronized void purchaseItem(String name, MarketItem item) throws RemoteException, RejectedException {
        if (!clients.containsKey(name)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + name + "\" is not registered.");
        } else {
            if (!hasItem(item)) {
                throw new RejectedException("MARKETPLACE(" + marketName + "): "
                        + "Required item(" + item.getName() + ", " + item.getPrice() + ", " + item.getOwner() + ") doesn't exist.");
            } else {
                //Get info
                Account buyerAccount = clients.get(name).account;

                ClientInfo sellerInfo = clients.get(item.getOwner());
                Account sellerAccount = sellerInfo.account;
                MarketplaceCallbackable seller = sellerInfo.callback;

                float price = item.getPrice();

                //Remove item from the market 
                removeItem(item);

                //Withdraw money from the buyer
                buyerAccount.withdraw(price);

                //Deposit money to the seller
                sellerAccount.deposit(price);

                //Set new owner of the item
                item.setOwner(name);

                //Notify seller
                seller.notifyPurchaseSuccessful(item);
            }
        }
    }

    @Override
    public synchronized void deleteWish(MarketItem wish) throws RemoteException, RejectedException {
        String owner = wish.getOwner();
        if (!clients.containsKey(owner)) {
            throw new RejectedException("MARKETPLACE(" + marketName + "): A client with name \"" + owner + "\" is not registered.");
        } else {
            if (!hasWish(wish)) {
                throw new RejectedException("MARKETPLACE(" + marketName + "): "
                        + "Required wish(" + wish.getName() + ", " + wish.getPrice() + ", " + wish.getOwner() + ") doesn't exist.");
            } else {
                //Remove wish from the market 
                removeWish(wish);
            }
        }
    }

    private boolean hasItem(MarketItem item) {
        Long id = item.getMarketItemId();
        return items.containsKey(id);
    }

    private boolean hasWish(MarketItem wish) {
        Long id = wish.getMarketItemId();
        return wishes.containsKey(id);
    }

    private void removeItem(MarketItem item) {
        Long id = item.getMarketItemId();
        items.remove(id);
    }

    private void removeWish(MarketItem wish) {
        Long id = wish.getMarketItemId();
        wishes.remove(id);
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
