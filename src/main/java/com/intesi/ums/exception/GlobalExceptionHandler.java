package com.intesi.ums.exception;

import com.intesi.ums.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;

/**
 * Centralised exception handling.
 *
 * All exceptions are mapped to a consistent {@link ErrorResponse} structure.
 * Errors are logged at WARN level (not ERROR) except for unexpected exceptions
 * which are logged at ERROR with the full stack trace.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ---- Domain exceptions ----

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        log.warn("User not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage(), request);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {
        log.warn("Duplicate resource: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE", ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateTransitionException.class)
    public ResponseEntity<ErrorResponse> handleIllegalTransition(IllegalStateTransitionException ex, HttpServletRequest request) {
        log.warn("Illegal state transition: {}", ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "ILLEGAL_STATE_TRANSITION", ex.getMessage(), request);
    }

    // ---- Validation & input exceptions ----

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fe -> ErrorResponse.FieldError.builder()
                .field(fe.getField())
                .message(fe.getDefaultMessage())
                .build())
            .toList();

        log.warn("Validation failed: {} field error(s)", fieldErrors.size());

        ErrorResponse body = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("VALIDATION_ERROR")
            .message("Request validation failed")
            .path(request.getRequestURI())
            .timestamp(Instant.now())
            .fieldErrors(fieldErrors)
            .build();

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed or missing request body: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST_BODY", "Request body is missing or malformed", request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName());
        log.warn("Type mismatch: {}", message);
        return build(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", message, request);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortField(PropertyReferenceException ex, HttpServletRequest request) {
        String message = String.format("Invalid sort field: '%s'", ex.getPropertyName());
        log.warn("Invalid sort field: {}", ex.getPropertyName());
        return build(HttpStatus.BAD_REQUEST, "INVALID_SORT_FIELD", message, request);
    }

    // ---- HTTP method / routing exceptions ----

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        String message = String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod());
        log.warn("Method not supported: {}", message);
        return build(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", message, request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("No resource found at: {}", request.getRequestURI());
        return build(HttpStatus.NOT_FOUND, "ENDPOINT_NOT_FOUND", "No endpoint found at " + request.getRequestURI(), request);
    }

    // ---- Database exceptions ----

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        // Handles race conditions where two concurrent requests pass the application-level
        // uniqueness check but one is then rejected by the DB constraint
        log.warn("Data integrity violation (likely concurrent duplicate): {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE",
            "A user with the same email, codiceFiscale or username already exists", request);
    }

    // ---- Security exceptions ----

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication required", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", "Insufficient permissions", request);
    }

    // ---- Catch-all ----

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error processing request {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred", request);
    }

    // ---- Helper ----

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(
            ErrorResponse.builder()
                .status(status.value())
                .error(error)
                .message(message)
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build()
        );
    }
}
