package com.intesi.ums.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 * Standardised error response payload.
 * All API errors return this structure so consumers have a predictable contract.
 */
@Schema(description = "Standard error response")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

    @Schema(description = "HTTP status code", example = "404")
    int status,

    @Schema(description = "Short error code", example = "USER_NOT_FOUND")
    String error,

    @Schema(description = "Human-readable message", example = "User with id ... not found")
    String message,

    @Schema(description = "Request path", example = "/api/v1/users/abc")
    String path,

    @Schema(description = "Timestamp of the error")
    Instant timestamp,

    @Schema(description = "Field-level validation errors (present only on 400)")
    List<FieldError> fieldErrors

) {
    @Builder
    public record FieldError(String field, String message) {}
}
