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
 * @author Igor
 */
public class Server {
    static final String USAGE = "java server.Server [rmi-URL of a catalog]";
    static final String CATALOG = "Catalog";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String catalogName = (args.length > 0) ? args[0] : CATALOG;
            if (catalogName.equalsIgnoreCase("-h")) {
                System.out.println(USAGE);
                System.exit(1);
            }

            Catalog catalogObj = new CatalogImpl();
            Naming.rebind(catalogName, catalogObj);
            System.out.println(catalogObj + " is ready.");
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
