/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import bank.Account;
import bank.BankClient;
import java.util.List;
import server.Marketplace;
import server.MarketItem;
import server.MarketplaceCallbackable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import server.Trader;

/**
 *
 * @author julio
 */
public class ClientImpl extends UnicastRemoteObject implements Trader, MarketplaceCallbackable, BankClient {
    private String userName;

    public ClientImpl(String userName) throws RemoteException {
        super();
        this.userName = userName;
    }

    @Override
    public void notifyItemAvailable(MarketItem item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyPurchaseSuccessful(MarketItem item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Account createAccount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Account getAccount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteAccount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deposit(float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void withdraw(float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float balance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void postItem(MarketItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void postWish(MarketItem wish) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean register(Marketplace market) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean unregister() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void buyItem(MarketItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<MarketItem> getItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
