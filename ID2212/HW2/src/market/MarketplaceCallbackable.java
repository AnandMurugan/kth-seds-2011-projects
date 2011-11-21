/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package market;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author julio
 */
public interface MarketplaceCallbackable extends Remote {
    public void notifyItemAvailable(MarketItem item) throws RemoteException;   

    public void notifyPostedItemSold(MarketItem item) throws RemoteException;
}
