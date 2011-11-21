/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package market;

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
    private static final String USAGE = "java MarketplaceServer <marketplace_rmi_url>";
    private static final String MARKETPLACE = "KistaGalleria";

    public MarketplaceServer(String marketplaceName) {
        try {
            Marketplace marketplaceobj = new MarketplaceImpl(marketplaceName);
            // Register the newly created object at rmiregistry.
            Naming.rebind(marketplaceName, marketplaceobj);
            System.out.println(marketplaceobj + " is ready.");
        } catch (MalformedURLException ex) {
            Logger.getLogger(MarketplaceServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(MarketplaceServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        if (args.length > 1 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String marketplaceName = null;
        if (args.length > 0) {
            marketplaceName = args[0];
        } else {
            marketplaceName = MARKETPLACE;
        }

        new MarketplaceServer(marketplaceName);
    }
}
