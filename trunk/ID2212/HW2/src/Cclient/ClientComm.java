/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cclient;

import Ccommon.IMarketItem;
import Ccommon.RemoteClient;
import Ccommon.RemoteMarket;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author julio
 */
public class ClientComm {
    
    List<IMarketItem> addedItems;
    RemoteMarket market;
    RemoteClient client;
    
    public ClientComm() throws NotBoundException, MalformedURLException, RemoteException{
        market = (RemoteMarket) Naming.lookup("test");
        
    }
    
    public void addItem(String itemName, Integer price){
        try {
            market.addItemToMarket(itemName, price, this.client);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addWish(String itemName, Integer limitPrice){
        try {
            market.addItemToWishList(itemName, limitPrice, client);
        } catch (RemoteException ex){
            Logger.getLogger(ClientComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
