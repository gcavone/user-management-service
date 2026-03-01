package com.intesi.ums.service;

import com.intesi.ums.domain.User;
import com.intesi.ums.domain.UserStatus;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.PagedResponse;
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

        if (user.getStatus() == UserStatus.DISABLED && request.username() != null) {
            log.warn("Updating username on a DISABLED user id={}", id);
        }

        if (request.username() != null && !request.username().equalsIgnoreCase(user.getUsername())) {
            if (userRepository.existsByUsernameIgnoreCase(request.username())) {
                throw new DuplicateResourceException("username", request.username());
            }
        }

        userMapper.updateEntityFromRequest(user, request);

        // Explicitly replace roles if provided
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
    public UserResponse disableUser(UUID id) {
        log.info("Disabling user id={}", id);
        User user = findActiveOrThrow(id);

        if (user.getStatus() == UserStatus.DISABLED) {
            log.debug("User id={} is already DISABLED, no-op", id);
            return userMapper.toResponse(user);
        }

        user.disable();
        User saved = userRepository.save(user);
        userRepository.flush();
        entityManager.refresh(saved);
        log.info("User id={} disabled", id);
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        log.info("Soft-deleting user id={}", id);
        User user = findActiveOrThrow(id);

        if (user.getStatus() == UserStatus.DELETED) {
            throw new IllegalStateTransitionException(user.getStatus(), "delete");
        }

        user.delete();
        userRepository.save(user);
        log.info("User id={} soft-deleted", id);
    }

    // ---- private helpers ----

    private User findActiveOrThrow(UUID id) {
        return userRepository.findActiveById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    private void validateUniqueConstraints(CreateUserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new DuplicateResourceException("email", request.email());
        }
        if (userRepository.existsActiveByCodiceFiscale(request.codiceFiscale())) {
            throw new DuplicateResourceException("codiceFiscale", request.codiceFiscale());
        }
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new DuplicateResourceException("username", request.username());
        }
    }
}
