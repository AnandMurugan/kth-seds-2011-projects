/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.RemoteMarket;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julio
 */
public class Server {
    public void main(String[] args) {
        try {
            if (args.length > 0) {
                RemoteMarket market = (RemoteMarket) (new MarketImpl(args[0]));
                Naming.bind(args[0], market);
            }
            else {
                System.out.println("Missing parameters");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.rmi.AlreadyBoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
        }
    }
}
