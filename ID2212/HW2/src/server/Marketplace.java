/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import bank.Account;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import utils.RejectedException;

/**
 *
 * @author julio
 */
public interface Marketplace extends Remote {
    void registerClient(String name, MarketplaceCallbackable callback, Account account) throws RemoteException, RejectedException;

    void unregisterClient(String name) throws RemoteException, RejectedException;

    long addItem(String name, MarketItem item) throws RemoteException, RejectedException;

    long addWish(String name, MarketItem wish) throws RemoteException, RejectedException;

    List<MarketItem> getItems() throws RemoteException;

    void purchaseItem(String name, MarketItem item) throws RemoteException, RejectedException;

    void deleteItem(String name, MarketItem item) throws RemoteException, RejectedException;

    void deleteWish(String name, MarketItem wish) throws RemoteException, RejectedException;
}
