/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import bank.Account;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public interface Marketplace extends Remote {
    public void registerClient(String name, MarketplaceCallbackable callback, Account account) throws RemoteException, RejectedException;

    public void unregisterClient(String name) throws RemoteException, RejectedException;

    public void addItem(MarketItem item) throws RemoteException, RejectedException;

    public void addWish(MarketItem wish) throws RemoteException, RejectedException;

    public Collection<MarketItem> getItems() throws RemoteException;

    public void purchaseItem(String name, MarketItem item) throws RemoteException, RejectedException;

    public void deleteWish(MarketItem wish) throws RemoteException, RejectedException;
}
