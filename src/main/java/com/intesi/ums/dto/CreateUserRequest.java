package com.intesi.ums.dto;

import com.intesi.ums.domain.ApplicationRole;
import com.intesi.ums.validation.ValidCodiceFiscale;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.Set;

@Schema(description = "Request payload for creating a new user")
@Builder
public record CreateUserRequest(

    @Schema(description = "Unique username", example = "mario.rossi")
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "username may only contain letters, digits, dots, underscores and hyphens")
    String username,

    @Schema(description = "Email address (immutable after creation)", example = "mario.rossi@example.com")
    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid address")
    @Size(max = 255)
    String email,

    @Schema(description = "Italian Codice Fiscale (16 chars, checksum validated)", example = "RSSMRA85M01H501Z")
    @NotBlank(message = "codiceFiscale is required")
    @ValidCodiceFiscale
    String codiceFiscale,

    @Schema(example = "Mario")
    @NotBlank(message = "nome is required")
    @Size(max = 100)
    String nome,

    @Schema(example = "Rossi")
    @NotBlank(message = "cognome is required")
    @Size(max = 100)
    String cognome,

    @Schema(description = "One or more application roles", example = "[\"DEVELOPER\"]")
    @NotEmpty(message = "at least one role must be assigned")
    Set<ApplicationRole> roles

) {}
