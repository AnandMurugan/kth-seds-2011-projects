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
public interface MarketItem extends Remote {
    public String getName() throws RemoteException;

    public float getPrice() throws RemoteException;

    public String getOwner() throws RemoteException;

    public void setOwner(String owner) throws RemoteException;
}
