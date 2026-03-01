package com.intesi.ums.exception;

import com.intesi.ums.domain.UserStatus;

public class IllegalStateTransitionException extends RuntimeException {
    public IllegalStateTransitionException(UserStatus current, String action) {
        super("Cannot perform '" + action + "' on a user with status " + current);
    }
}
