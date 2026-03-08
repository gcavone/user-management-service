package com.intesi.ums.service;

import com.intesi.ums.domain.User;
import com.intesi.ums.domain.ApplicationRole;
import com.intesi.ums.domain.UserStatus;
import com.intesi.ums.exception.ForbiddenException;
import com.intesi.ums.exception.PrivilegeEscalationException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.PagedResponse;
import com.intesi.ums.dto.UpdateStatusRequest;
import com.intesi.ums.dto.UpdateUserRequest;
import com.intesi.ums.dto.UserResponse;
import com.intesi.ums.exception.DuplicateResourceException;
import com.intesi.ums.exception.IllegalStateTransitionException;
import com.intesi.ums.exception.UserNotFoundException;
import com.intesi.ums.mapper.UserMapper;
import com.intesi.ums.messaging.UserEventPublisher;
import com.intesi.ums.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher eventPublisher;
    private final EntityManager entityManager;

    @Override
    public PagedResponse<UserResponse> listUsers(UserStatus status, String search, Pageable pageable, boolean maskSensitiveFields) {
        log.debug("Listing users - status={}, search='{}', page={}, size={}", status, search, pageable.getPageNumber(), pageable.getPageSize());

        Page<UserResponse> page = userRepository
            .findAllActive(status, search, pageable)
            .map(user -> maskSensitiveFields ? userMapper.toMaskedResponse(user) : userMapper.toResponse(user));

        return PagedResponse.from(page);
    }

    @Override
    public UserResponse getUserById(UUID id, boolean maskSensitiveFields) {
        log.debug("Fetching user by id={}", id);
        User user = findActiveOrThrow(id);
        return maskSensitiveFields ? userMapper.toMaskedResponse(user) : userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with username='{}', email='{}'", request.username(), request.email());

        validateRoleAssignment(request.roles());
        validateUniqueConstraints(request);

        User user = userMapper.toEntity(request);
        // Normalise CF to uppercase for consistent storage and comparison
        user.setCodiceFiscale(request.codiceFiscale().toUpperCase());

        User saved = userRepository.save(user);
        userRepository.flush();
        entityManager.refresh(saved);
        log.info("User created successfully with id={}", saved.getId());

        // Publish domain event AFTER commit — failure here does not roll back creation
        eventPublisher.publishUserCreated(saved);

        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        log.info("Updating user id={}", id);
        User user = findActiveOrThrow(id);

        // Caller cannot update a user whose highest role has greater privilege than their own
        ApplicationRole callerRole = resolveCallerHighestRole();
        validateTargetHierarchy(user, callerRole);

        // Validate role assignment privilege escalation before any mutation
        if (request.roles() != null && !request.roles().isEmpty()) {
            validateRoleAssignment(request.roles());
        }

        if (user.getStatus() == UserStatus.DISABLED && request.username() != null) {
            log.warn("Updating username on a DISABLED user id={}", id);
        }

        if (request.username() != null && !request.username().equalsIgnoreCase(user.getUsername())) {
            if (userRepository.existsActiveByUsernameIgnoreCase(request.username())) {
                throw new DuplicateResourceException("username", request.username());
            }
        }

        userMapper.updateEntityFromRequest(user, request);

        // Apply roles (already validated above)
        if (request.roles() != null && !request.roles().isEmpty()) {
            user.getRoles().clear();
            user.getRoles().addAll(request.roles());
        }

        User saved = userRepository.save(user);
        userRepository.flush();
        entityManager.refresh(saved);
        log.info("User id={} updated successfully", id);
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponse updateUserStatus(UUID id, UpdateStatusRequest request) {
        log.info("Status update requested for user id={} → {}", id, request.status());
        User target = findActiveOrThrow(id);

        // Transitions from DELETED are always illegal — soft delete is irreversible via API
        if (target.getStatus() == UserStatus.DELETED) {
            throw new IllegalStateTransitionException(target.getStatus(), request.status().name());
        }

        // Authorization checks come before no-op — a caller must have permission
        // regardless of whether the operation would actually change anything
        ApplicationRole callerRole = resolveCallerHighestRole();

        // Only OWNER can soft-delete
        if (request.status() == UserStatus.DELETED && callerRole != ApplicationRole.OWNER) {
            throw new ForbiddenException("Insufficient permissions");
        }

        // Caller cannot act on a user whose highest role has greater privilege than their own
        validateTargetHierarchy(target, callerRole);

        // No-op: target already has the requested status (checked after auth)
        if (target.getStatus() == request.status()) {
            log.debug("User id={} already has status={}, no-op", id, request.status());
            return userMapper.toResponse(target);
        }

        switch (request.status()) {
            case ACTIVE   -> target.setStatus(UserStatus.ACTIVE);
            case DISABLED -> target.disable();
            case DELETED  -> target.delete();
        }

        User saved = userRepository.save(target);
        userRepository.flush();
        entityManager.refresh(saved);
        log.info("User id={} status updated to {}", id, saved.getStatus());
        return userMapper.toResponse(saved);
    }

    // ---- private helpers ----

    private User findActiveOrThrow(UUID id) {
        return userRepository.findActiveById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Resolves the caller's highest-privilege role from the Spring Security context.
     * "Highest privilege" means the role with the lowest ordinal value.
     */
    private ApplicationRole resolveCallerHighestRole() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .map(a -> a.getAuthority().replace("ROLE_", ""))
            .map(ApplicationRole::valueOf)
            .min(Comparator.comparingInt(ApplicationRole::ordinal))
            .orElseThrow();
    }

    /**
     * Prevents a caller from acting on a user whose highest role has greater privilege.
     * Example: OPERATOR (ordinal 1) cannot act on a user whose highest role is OWNER (ordinal 0).
     */
    private void validateTargetHierarchy(User target, ApplicationRole callerRole) {
        target.getRoles().stream()
            .min(Comparator.comparingInt(ApplicationRole::ordinal))
            .ifPresent(targetHighestRole -> {
                if (targetHighestRole.ordinal() < callerRole.ordinal()) {
                    throw new ForbiddenException("Insufficient permissions");
                }
            });
    }

    /**
     * Ensures the caller cannot assign roles with higher privilege than their own.
     * Delegates to resolveCallerHighestRole for consistency.
     */
    private void validateRoleAssignment(java.util.Set<ApplicationRole> rolesToAssign) {
        ApplicationRole callerRole = resolveCallerHighestRole();

        rolesToAssign.forEach(role -> {
            if (!role.isAssignableBy(callerRole)) {
                throw new PrivilegeEscalationException(role);
            }
        });
    }

    private void validateUniqueConstraints(CreateUserRequest request) {
        if (userRepository.existsActiveByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException("email", request.email());
        }
        if (userRepository.existsByCodiceFiscale(request.codiceFiscale())) {
            throw new DuplicateResourceException("codiceFiscale", request.codiceFiscale());
        }
        if (userRepository.existsActiveByUsernameIgnoreCase(request.username())) {
            throw new DuplicateResourceException("username", request.username());
        }
    }
}