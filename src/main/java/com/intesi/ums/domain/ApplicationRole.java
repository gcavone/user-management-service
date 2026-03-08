package com.intesi.ums.domain;

/**
 * Application-level roles that can be assigned to a user.
 *
 * Note: these are domain roles (business-level permissions within the product)
 * and are distinct from security/IAM roles used by Keycloak for API authentication.
 *
 * Roles are ordered by privilege level (higher ordinal = lower privilege).
 * A caller can only assign roles with ordinal >= their own (i.e. equal or lower privilege).
 */
public enum ApplicationRole {
    OWNER,
    OPERATOR,
    MAINTAINER,
    DEVELOPER,
    REPORTER;

    /**
     * Returns true if this role has equal or lower privilege than the given role.
     * Used to prevent privilege escalation when assigning roles.
     */
    public boolean isAssignableBy(ApplicationRole callerRole) {
        return this.ordinal() >= callerRole.ordinal();
    }
}