package org.igorkos.test;

import org.igorkos.core.IK2001SecurityManager;

import java.security.AccessControlException;

public class IK2001PermissionTest {

    public static void main(String args[]) {
        if (args.length < 1) {
            System.err.println("usage:  IK2001PermissionTest <name>");
            System.exit(-1);
        }

        String permissionName = args[0];

        IK2001SecurityManager security = (IK2001SecurityManager) System.getSecurityManager();
        try {
            security.checkIK2001(permissionName);
            System.out.println(permissionName + " is allowed");
        } catch (AccessControlException ace) {
            System.out.println(permissionName + " is NOT allowed");
        }
    }
}
