package com.intesi.ums.domain;

/**
 * Application-level roles that can be assigned to a user.
 *
 * Note: these are domain roles (business-level permissions within the product)
 * and are distinct from security/IAM roles used by Keycloak for API authentication.
 */
public enum ApplicationRole {
    OWNER,
    OPERATOR,
    MAINTAINER,
    DEVELOPER,
    REPORTER
}
