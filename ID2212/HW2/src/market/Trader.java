/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package market;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Igor
 */
public interface Trader extends Remote {
    void postItem(MarketItem item) throws RemoteException;

    void postWish(MarketItem wish) throws RemoteException;

    void buyItem(MarketItem item) throws RemoteException;

    void unpostWish(MarketItem wish) throws RemoteException;

    void unpostItem(MarketItem item) throws RemoteException;

    void register() throws RemoteException;

    void unregister() throws RemoteException;
}
