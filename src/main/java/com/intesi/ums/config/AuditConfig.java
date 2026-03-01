package com.intesi.ums.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * Provides the current authenticated user's identifier for Spring Data Auditing.
 * The value is used to populate @CreatedBy and @LastModifiedBy on entities.
 *
 * For Keycloak JWTs, we use the "preferred_username" claim.
 */
@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return Optional.of("system");
            }
            Object principal = auth.getPrincipal();
            if (principal instanceof Jwt jwt) {
                String username = jwt.getClaimAsString("preferred_username");
                return Optional.ofNullable(username).or(() -> Optional.of(jwt.getSubject()));
            }
            return Optional.of(auth.getName());
        };
    }
}
