/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julio
 */
public class MarketplaceServer {
    public void main(String[] args) {
        try {
            if (args.length > 0) {
                Marketpalce market = new MarketplaceImpl(args[0]);
                Naming.bind(args[0], market);
            } else {
                System.out.println("Missing parameters");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(MarketplaceServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.rmi.AlreadyBoundException ex) {
            Logger.getLogger(MarketplaceServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
        }
    }
}
