/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import bank.Account;
import bank.Bank;
import bank.BankClient;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.List;
import market.Marketplace;
import market.MarketItem;
import market.MarketplaceCallbackable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import market.Trader;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public class ClientImpl extends UnicastRemoteObject implements Trader, MarketplaceCallbackable, BankClient {
    private static final String DEFAULT_HOST = "130.229.129.151";
    private static final int DEFAULT_PORT = 1099;
    private static final String MARKETPLACE = "KistaGalleria";
    private static final String BANK = "Swedbank";
    private String userName;
    private ClientResponsiveUI ui;
//    private List<MarketItem> postedItems;
//    private List<MarketItem> wishedItems;
//    private List<MarketItem> ownedItems;
//    private List<MarketItem> allItems;
    private Marketplace market;
    private Account account;
    private Bank bank;

    public ClientImpl(ClientResponsiveUI ui) throws RemoteException {
        super();
        //this.userName = "";
        this.ui = ui;
    }

    @Override
    public void notifyItemAvailable(MarketItem item) throws RemoteException {
        ui.showBuyWishItemDialog(item);
    }

    /*@Override
    public void notifyPurchaseSuccessful(MarketItem item) throws RemoteException {
    ui.showBuyConfirmationDialog();
    this.ownedItems.add(item);
    }*/
    @Override
    public void notifyPostedItemSold(MarketItem item) throws RemoteException {
        ui.showItemSoldNotificationMessage(item);
        ui.updateOnPostedItemSold(item);
        ui.updateAllItems(market.getItems());
        ui.updateBalance(account.getBalance());
    }

    @Override
    public Account createAccount() {
        try {
            this.account = bank.newAccount(this.userName);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.account;
    }

    @Override
    public Account getAccount() {
        try {
//            if (bank == null) {
            bank = (Bank) Naming.lookup("//" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + BANK);
//            }
            if (this.account == null) {
                try {
                    this.account = bank.getAccount(this.userName);
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            ui.showExceptionNotificationMessage(ex.getMessage());
        }

        return this.account;
    }

    @Override
    public boolean deleteAccount() {
        boolean result = false;
        try {
            result = bank.deleteAccount(this.userName);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public void deposit(float value) {
        try {
            this.account.deposit(value);
            ui.updateBalance(this.account.getBalance());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void withdraw(float value) {
        try {
            this.account.withdraw(value);
            ui.updateBalance(this.account.getBalance());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public float balance() {
        float balance = (float) 0.0;

        try {
            balance = this.account.getBalance();
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return balance;
    }

    @Override
    public void postItem(MarketItem item) {
        try {
            long marketItemId = market.addItem(this.userName, item);
            item.setMarketItemId(marketItemId);
            ui.updateOnPostItem(item);
            ui.updateAllItems(market.getItems());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            try {
                //Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                ui.showRejectedNotificationMessage(ex.getMessage());
                ui.updateAllItems(market.getItems());
                ui.updateBalance(account.getBalance());
            } catch (RemoteException ex1) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @Override
    public void postWish(MarketItem wish) {
        try {
            long marketItemId = market.addWish(this.userName, wish);
            wish.setMarketItemId(marketItemId);
            ui.updateOnPostWish(wish);
            ui.updateAllItems(market.getItems());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            try {
                //Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                ui.showRejectedNotificationMessage(ex.getMessage());
                ui.updateAllItems(market.getItems());
                ui.updateBalance(account.getBalance());
            } catch (RemoteException ex1) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @Override
    public void unpostItem(MarketItem item) {
        try {
            market.deleteItem(this.userName, item);
            ui.updateOnUnpostItem(item);
            ui.updateAllItems(market.getItems());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            try {
                //Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                ui.showRejectedNotificationMessage(ex.getMessage());
                ui.updateAllItems(market.getItems());
                ui.updateBalance(account.getBalance());
            } catch (RemoteException ex1) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @Override
    public void unpostWish(MarketItem wish) {
        try {
            market.deleteWish(this.userName, wish);
            ui.updateOnUnpostWish(wish);
            ui.updateAllItems(market.getItems());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            try {
                //Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                ui.showRejectedNotificationMessage(ex.getMessage());
                ui.updateAllItems(market.getItems());
                ui.updateBalance(account.getBalance());
            } catch (RemoteException ex1) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @Override
    public void register() {
        try {
            market = (Marketplace) Naming.lookup("//" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + MARKETPLACE);
            // TODO. Modify register client to return result
            market.registerClient(userName, this, this.account);
            ui.updateTitle("Connected to " + MARKETPLACE);
            ui.updateAllItems(market.getItems());
            ui.updateBalance(account.getBalance());
        } catch (RejectedException ex) {
            try {
                //Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                ui.showRejectedNotificationMessage(ex.getMessage());
                ui.updateAllItems(market.getItems());
                ui.updateBalance(account.getBalance());
            } catch (RemoteException ex1) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception ex) {
            ui.showExceptionNotificationMessage(ex.getMessage());
        }
    }

    @Override
    public void unregister() {
        try {
            market.unregisterClient(this.userName);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            try {
                //Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                ui.showRejectedNotificationMessage(ex.getMessage());
                ui.updateAllItems(market.getItems());
                ui.updateBalance(account.getBalance());
            } catch (RemoteException ex1) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @Override
    public void buyItem(MarketItem item) {
        try {
            market.purchaseItem(this.userName, item);
            ui.updateBalance(account.getBalance());
            ui.updateOnItemBought(item);
            ui.updateAllItems(market.getItems());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            try {
                //Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
                ui.showRejectedNotificationMessage(ex.getMessage());
                ui.updateAllItems(market.getItems());
                ui.updateBalance(account.getBalance());
            } catch (RemoteException ex1) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void getItems() {
        try {
            ui.updateAllItems(market.getItems());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
