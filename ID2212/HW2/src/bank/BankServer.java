/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class BankServer {
    private static final String USAGE = "java BankServer <bank_rmi_url>";
    private static final String BANK = "Swedbank";

    public BankServer(String bankName) {
        try {
            Bank bankobj = new BankImpl(bankName);
            // Register the newly created object at rmiregistry.
            java.rmi.Naming.rebind(bankName, bankobj);
            System.out.println(bankobj + " is ready.");
        } catch (MalformedURLException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        if (args.length > 1 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
            System.out.println(USAGE);
            System.exit(1);
        }

        String bankName = null;
        if (args.length > 0) {
            bankName = args[0];
        } else {
            bankName = BANK;
        }

        new BankServer(bankName);
    }
}
