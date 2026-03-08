package com.intesi.ums.service;

import com.intesi.ums.domain.UserStatus;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.PagedResponse;
import com.intesi.ums.dto.UpdateStatusRequest;
import com.intesi.ums.dto.UpdateUserRequest;
import com.intesi.ums.dto.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    /**
     * Returns a paginated list of non-deleted users.
     *
     * @param status optional filter by status
     * @param search optional free-text search on username, email, nome, cognome
     * @param pageable pagination and sorting parameters
     * @param maskSensitiveFields when true, email and CF are masked in the response
     */
    PagedResponse<UserResponse> listUsers(UserStatus status, String search, Pageable pageable, boolean maskSensitiveFields);

    /**
     * Returns a single non-deleted user by ID.
     */
    UserResponse getUserById(UUID id, boolean maskSensitiveFields);

    /**
     * Creates a new user and publishes a UserCreatedEvent.
     */
    UserResponse createUser(CreateUserRequest request);

    /**
     * Partially updates a user's mutable fields.
     * email and codiceFiscale cannot be changed.
     */
    UserResponse updateUser(UUID id, UpdateUserRequest request);

    /**
     * Updates the status of a user (ACTIVE / DISABLED / DELETED).
     *
     * Business rules enforced here (not at the controller level):
     * - Transitions from DELETED are always illegal — soft delete is irreversible via API.
     * - Transitioning to DELETED requires OWNER role.
     * - Caller cannot act on a user whose highest role has greater privilege than their own.
     */
    UserResponse updateUserStatus(UUID id, UpdateStatusRequest request);
}
