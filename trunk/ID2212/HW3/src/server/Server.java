/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.AccessPermission;
import model.WriteReadPermission;
import utils.RejectedException;

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

            catalogObj.registerUser("Igor", "igorigor");
            int id = catalogObj.login("Igor", "igorigor");
            catalogObj.uploadFile(id, "TEST FILE", AccessPermission.PUBLIC, WriteReadPermission.WRITE, new File("d:/Programming/NetBeansProjects/ID2212_HW3/build.xml"));
        } catch (RejectedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
