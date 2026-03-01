package com.intesi.ums.service;

import com.intesi.ums.domain.UserStatus;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.PagedResponse;
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
     * Disables the user (status -> DISABLED). Idempotent.
     */
    UserResponse disableUser(UUID id);

    /**
     * Soft-deletes the user (status -> DELETED). Irreversible via API.
     */
    void deleteUser(UUID id);
}
