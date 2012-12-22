package org.igorkos.core;

import org.igorkos.core.permission.IK2001Permission;

public class IK2001SecurityManager extends SecurityManager {

    public void checkIK2001(String name) {
        checkPermission(new IK2001Permission(name));
    }
}
