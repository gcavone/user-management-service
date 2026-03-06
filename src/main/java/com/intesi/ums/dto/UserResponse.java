package com.intesi.ums.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intesi.ums.domain.ApplicationRole;
import com.intesi.ums.domain.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * User response DTO.
 *
 * Sensitive fields (email, codiceFiscale) are returned as-is for users with
 * sufficient privileges (OWNER, OPERATOR, MAINTAINER, DEVELOPER roles). REPORTER-role callers receive
 * masked values via the UserMapper masking variant.
 */
@Schema(description = "User data returned by the API")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(

    @Schema(description = "Unique identifier")
    UUID id,

    String username,

    @Schema(description = "Email address. May be masked (***) depending on caller privileges.")
    String email,

    @Schema(description = "Codice Fiscale. May be masked (***) depending on caller privileges.")
    String codiceFiscale,

    String nome,
    String cognome,

    UserStatus status,

    Set<ApplicationRole> roles,

    Instant createdAt,
    Instant updatedAt,
    String createdBy

) {
    /** Produces a masked email: keeps domain part visible (e.g. m***@example.com) */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        int atIndex = email.indexOf('@');
        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    /** Masks all but last 3 chars of the CF */
    public static String maskCodiceFiscale(String cf) {
        if (cf == null || cf.length() < 4) return "***";
        return "*".repeat(cf.length() - 3) + cf.substring(cf.length() - 3);
    }
}