package com.intesi.ums.controller;

import com.intesi.ums.config.SecurityConfig;
import com.intesi.ums.domain.UserStatus;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.ErrorResponse;
import com.intesi.ums.dto.PagedResponse;
import com.intesi.ums.dto.UpdateUserRequest;
import com.intesi.ums.dto.UserResponse;
import com.intesi.ums.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management operations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'OPERATOR', 'MAINTAINER', 'DEVELOPER', 'REPORTER')")
    @Operation(
        summary = "List users",
        description = """
            Returns a paginated list of users.
            REPORTER role receives masked email and CF.
            All other roles receive full data.
            """,
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<PagedResponse<UserResponse>> listUsers(
        @Parameter(description = "Filter by status") @RequestParam(required = false) UserStatus status,
        @Parameter(description = "Free-text search on username, email, nome, cognome") @RequestParam(required = false) String search,
        @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
        @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") Sort.Direction direction,
        Authentication authentication
    ) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(direction, sortBy));
        boolean mask = shouldMask(authentication);
        return ResponseEntity.ok(userService.listUsers(status, search, pageable, mask));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'OPERATOR', 'MAINTAINER', 'DEVELOPER', 'REPORTER')")
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "404", description = "User not found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UserResponse> getUser(
        @PathVariable UUID id,
        Authentication authentication
    ) {
        boolean mask = shouldMask(authentication);
        return ResponseEntity.ok(userService.getUserById(id, mask));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'OPERATOR', 'MAINTAINER')")
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "409", description = "Duplicate email/CF/username",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'OPERATOR', 'MAINTAINER')")
    @Operation(
        summary = "Update user data and/or roles",
        description = "Partial update. email and codiceFiscale are immutable and cannot be changed."
    )
    public ResponseEntity<UserResponse> updateUser(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasAnyRole('OWNER', 'OPERATOR')")
    @Operation(summary = "Disable a user", description = "Sets user status to DISABLED. Idempotent.")
    public ResponseEntity<UserResponse> disableUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.disableUser(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(
        summary = "Soft-delete a user",
        description = "Marks user as DELETED. The record is retained for audit purposes but excluded from all queries."
    )
    @ApiResponse(responseCode = "204", description = "User deleted")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ---- helpers ----

    /**
     * Returns true if the caller's role should receive masked sensitive fields.
     * Only REPORTER receives masked data — all other roles see full data.
     */
    private boolean shouldMask(Authentication auth) {
        if (auth == null) return true;
        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .noneMatch(SecurityConfig.UNMASKED_ROLES::contains);
    }
}
