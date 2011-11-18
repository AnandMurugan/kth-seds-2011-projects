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
import server.Marketplace;
import server.MarketItem;
import server.MarketplaceCallbackable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Trader;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public class ClientImpl extends UnicastRemoteObject implements Trader, MarketplaceCallbackable, BankClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;
    private static final String MARKETPLACE = "KistaGalleria";
    private static final String BANK = "Swedbank";
    private String userName;
    ClientResponsiveUI ui;
    private List<MarketItem> postedItems;
    private List<MarketItem> wishedItems;
    private List<MarketItem> ownedItems;
    private List<MarketItem> allItems;
    private Marketplace market;
    private Account account;
    private Bank bank;

    public ClientImpl(ClientResponsiveUI ui) throws RemoteException {
        super();
        this.userName = "";
        this.ui = ui;
        try {
            market = (Marketplace) Naming.lookup("//" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + MARKETPLACE);
            bank = (Bank) Naming.lookup("//" + DEFAULT_HOST + ":" + DEFAULT_PORT + "/" + BANK);
        } catch (NotBoundException ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        ui.updatePostedItemSold(item);
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
        if (this.account == null) {
            try {
                this.account = bank.getAccount(this.userName);
            } catch (RemoteException ex) {
                Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            market.addItem(this.userName, item);

        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void postWish(MarketItem wish) {
        try {
            market.addWish(this.userName, wish);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean register() {
        boolean result = false;

        try {
            // TODO. Modify register client to return result
            market.registerClient(userName, this, this.account);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean unregister() {
        try {
            market.unregisterClient(this.userName);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    public void buyItem(MarketItem item) {
        try {
            market.purchaseItem(this.userName, item);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RejectedException ex) {
            // TODO. handle RejectedException;
        }
    }

    @Override
    public List<MarketItem> getItems() {
        try {
            this.allItems = new ArrayList(market.getItems());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.allItems;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
