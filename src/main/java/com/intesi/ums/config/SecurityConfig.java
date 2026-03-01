package com.intesi.ums.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security configuration for OAuth2 Resource Server with Keycloak.
 *
 * Architecture:
 * - Stateless JWT-based authentication (no sessions)
 * - Keycloak issues JWTs; this service validates them against the JWKS endpoint
 * - Roles are extracted from the Keycloak "realm_access.roles" claim
 * - Method-level security (@PreAuthorize) is enabled for fine-grained RBAC
 *
 * Role hierarchy (same roles defined in the domain):
 *   OWNER      - full access: create, read, update, disable, delete + unmasked data
 *   OPERATOR   - create, read, update, disable + unmasked data
 *   MAINTAINER - create, read, update + unmasked data
 *   DEVELOPER  - read-only + unmasked data
 *   REPORTER   - read-only + masked email and CF
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Roles that can see sensitive fields (email, CF) unmasked
    public static final Set<String> UNMASKED_ROLES = Set.of(
        "ROLE_OWNER", "ROLE_OPERATOR", "ROLE_MAINTAINER", "ROLE_DEVELOPER"
    );

    private static final Set<String> APPLICATION_ROLES = Set.of(
        "OWNER", "OPERATOR", "MAINTAINER", "DEVELOPER", "REPORTER"
    );

    private static final String[] PUBLIC_PATHS = {
        "/actuator/health",
        "/actuator/info",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_PATHS).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtConverter()))
            );

        return http.build();
    }

    /**
     * Extracts roles from Keycloak's realm_access.roles claim and maps them to
     * Spring Security GrantedAuthority objects with the ROLE_ prefix.
     *
     * Only the five application roles are extracted; Keycloak built-in roles
     * (offline_access, uma_authorization, etc.) are intentionally ignored.
     *
     * Example Keycloak JWT payload excerpt:
     * {
     *   "realm_access": {
     *     "roles": ["OWNER", "offline_access", "uma_authorization"]
     *   }
     * }
     */
    @Bean
    public JwtAuthenticationConverter keycloakJwtConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) {
                return List.of();
            }
            @SuppressWarnings("unchecked")
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            return roles.stream()
                .filter(APPLICATION_ROLES::contains)
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        });
        return converter;
    }
}
