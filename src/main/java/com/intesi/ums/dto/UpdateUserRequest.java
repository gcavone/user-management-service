package com.intesi.ums.dto;

import com.intesi.ums.domain.ApplicationRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Partial update request. All fields are optional.
 * email is intentionally absent — it is immutable by business rule.
 * codiceFiscale is also not updatable (business identity document).
 */
@Schema(description = "Request payload for updating an existing user. All fields are optional.")
public record UpdateUserRequest(

    @Schema(description = "New username", example = "mario.rossi2")
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "username may only contain letters, digits, dots, underscores and hyphens")
    String username,

    @Schema(example = "Mario")
    @Size(max = 100)
    String nome,

    @Schema(example = "Rossi")
    @Size(max = 100)
    String cognome,

    @Schema(description = "Replaces current role set. Provide all desired roles.")
    @NotEmpty(message = "roles must not be empty if provided")
    Set<ApplicationRole> roles

) {}
