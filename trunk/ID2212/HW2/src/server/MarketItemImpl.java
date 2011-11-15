/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author julio
 */
public class MarketItemImpl extends UnicastRemoteObject implements MarketItem {
    private String name;
    private float price;
    private String owner;

    public MarketItemImpl(String name, float price, String owner) throws RemoteException {
        //super();
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public float getPrice() throws RemoteException {
        return price;
    }

    @Override
    public String getOwner() throws RemoteException {
        return owner;
    }

    @Override
    public void setOwner(String owner) throws RemoteException {
        this.owner = owner;
    }
}
