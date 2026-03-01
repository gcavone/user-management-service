package com.intesi.ums.domain;

/**
 * Lifecycle status of a user.
 *
 * ACTIVE   -> normal operational state
 * DISABLED -> temporarily suspended; access denied but record retained
 * DELETED  -> soft-deleted; excluded from all normal queries and responses
 */
public enum UserStatus {
    ACTIVE,
    DISABLED,
    DELETED
}
