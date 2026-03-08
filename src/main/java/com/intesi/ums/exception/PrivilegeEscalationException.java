package com.intesi.ums.exception;

import com.intesi.ums.domain.ApplicationRole;

public class PrivilegeEscalationException extends RuntimeException {
    public PrivilegeEscalationException(ApplicationRole role) {
        super("You do not have sufficient privileges to assign role: " + role);
    }
}
