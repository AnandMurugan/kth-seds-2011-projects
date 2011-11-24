/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Naming;

/**
 *
 * @author Igor
 */
public class Server {
    static final String USAGE = "java server.Server [rmi-URL of a catalog]";
    static final String CATALOG = "Catalog";

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String catalogName = (args.length > 0) ? args[0] : CATALOG;
        if (catalogName.equalsIgnoreCase("-h")) {
            System.out.println(USAGE);
            System.exit(1);
        }

        Catalog catalogObj = new CatalogImpl();
        Naming.rebind(catalogName, catalogObj);
        System.out.println(catalogObj + " is ready.");
    }
}
