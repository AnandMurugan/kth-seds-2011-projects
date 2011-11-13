/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Server.MarketItem;
import Common.RemoteClient;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author julio
 */
public class ClientImpl extends UnicastRemoteObject implements RemoteClient{
    private String userName;
    
    public ClientImpl(String userName) throws RemoteException{
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
    
}
