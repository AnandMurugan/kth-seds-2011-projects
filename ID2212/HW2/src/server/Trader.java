/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Igor
 */
public interface Trader extends Remote {
    public void postItem(MarketItem item) throws RemoteException;

    public void postWish(MarketItem wish) throws RemoteException;

    public void buyItem(MarketItem item) throws RemoteException;

    public List<MarketItem> getItems() throws RemoteException;

    public boolean register(Marketplace market) throws RemoteException;

    public boolean unregister() throws RemoteException;
}
