package com.intesi.ums.dto;

import com.intesi.ums.domain.UserStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request body for PATCH /users/{id}/status.
 *
 * Allowed transitions:
 *   ACTIVE   → DISABLED  (OWNER, OPERATOR — with target hierarchy check)
 *   ACTIVE   → DELETED   (OWNER only)
 *   DISABLED → ACTIVE    (OWNER, OPERATOR — with target hierarchy check)
 *   DISABLED → DELETED   (OWNER only)
 *   DELETED  → *         illegal — soft delete is irreversible via API
 */
public record UpdateStatusRequest(
    @NotNull(message = "status is required")
    UserStatus status
) {}
