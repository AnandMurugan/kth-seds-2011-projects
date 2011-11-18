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

    public void addItem(String name, MarketItem item) throws RemoteException, RejectedException;

    public void addWish(String name, MarketItem wish) throws RemoteException, RejectedException;

    public Collection<MarketItem> getItems() throws RemoteException;

    public void purchaseItem(String name, MarketItem item) throws RemoteException, RejectedException;
    
    public void deleteItem(String name, MarketItem item) throws RemoteException, RejectedException;

    public void deleteWish(String name, MarketItem wish) throws RemoteException, RejectedException;
}
