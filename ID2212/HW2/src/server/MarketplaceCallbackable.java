/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author julio
 */
public interface MarketplaceCallbackable extends Remote {
    public void notifyItemAvailable(MarketItem item) throws RemoteException;

    public void notifyPurchaseSuccessful(MarketItem item) throws RemoteException;
    //public void notifyPurchaseRejected(MarketItem item, RejectReason reason) throws RemoteException;
}
