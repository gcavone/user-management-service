package com.intesi.ums.service;

import com.intesi.ums.domain.ApplicationRole;
import com.intesi.ums.domain.User;
import com.intesi.ums.domain.UserStatus;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.PagedResponse;
import com.intesi.ums.dto.UpdateUserRequest;
import com.intesi.ums.dto.UserResponse;
import com.intesi.ums.exception.DuplicateResourceException;
import com.intesi.ums.exception.UserNotFoundException;
import com.intesi.ums.mapper.UserMapper;
import com.intesi.ums.messaging.UserEventPublisher;
import com.intesi.ums.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService unit tests")
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private UserEventPublisher eventPublisher;
    @Mock private jakarta.persistence.EntityManager entityManager;

    @InjectMocks private UserServiceImpl userService;

    private User testUser;
    private UserResponse testUserResponse;
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(userId)
            .username("mario.rossi")
            .email("mario.rossi@example.com")
            .codiceFiscale("RSSMRA85M01H501Z")
            .nome("Mario")
            .cognome("Rossi")
            .status(UserStatus.ACTIVE)
            .roles(Set.of(ApplicationRole.DEVELOPER))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        testUserResponse = UserResponse.builder()
            .id(userId)
            .username("mario.rossi")
            .email("mario.rossi@example.com")
            .codiceFiscale("RSSMRA85M01H501Z")
            .nome("Mario")
            .cognome("Rossi")
            .status(UserStatus.ACTIVE)
            .roles(Set.of(ApplicationRole.DEVELOPER))
            .build();
    }

    @Nested
    @DisplayName("listUsers")
    class ListUsers {

        @Test
        @DisplayName("returns paged response with mapped users")
        void returnsPagedResponse() {
            Page<User> page = new PageImpl<>(List.of(testUser), PageRequest.of(0, 20), 1);
            when(userRepository.findAllActive(null, null, PageRequest.of(0, 20))).thenReturn(page);
            when(userMapper.toResponse(testUser)).thenReturn(testUserResponse);

            PagedResponse<UserResponse> result = userService.listUsers(null, null, PageRequest.of(0, 20), false);

            assertThat(result.content()).hasSize(1);
            assertThat(result.totalElements()).isEqualTo(1);
            assertThat(result.content().get(0).email()).isEqualTo("mario.rossi@example.com");
        }

        @Test
        @DisplayName("applies masking when maskSensitiveFields=true")
        void appliesMaskingForViewer() {
            UserResponse maskedResponse = UserResponse.builder()
                .id(userId).username("mario.rossi")
                .email("m***@example.com").codiceFiscale("*************01Z")
                .build();

            Page<User> page = new PageImpl<>(List.of(testUser), PageRequest.of(0, 20), 1);
            when(userRepository.findAllActive(null, null, PageRequest.of(0, 20))).thenReturn(page);
            when(userMapper.toMaskedResponse(testUser)).thenReturn(maskedResponse);

            PagedResponse<UserResponse> result = userService.listUsers(null, null, PageRequest.of(0, 20), true);

            assertThat(result.content().get(0).email()).startsWith("m***");
            verify(userMapper, never()).toResponse(any());
        }
    }

    @Nested
    @DisplayName("createUser")
    class CreateUser {

        @Test
        @DisplayName("creates user successfully and publishes event")
        void createsSuccessfully() {
            CreateUserRequest request = new CreateUserRequest(
                "mario.rossi", "mario.rossi@example.com", "RSSMRA85M01H501Z",
                "Mario", "Rossi", Set.of(ApplicationRole.DEVELOPER)
            );

            when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
            when(userRepository.existsActiveByCodiceFiscale(anyString())).thenReturn(false);
            when(userRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
            when(userMapper.toEntity(request)).thenReturn(testUser);
            when(userRepository.save(testUser)).thenReturn(testUser);
            when(userMapper.toResponse(testUser)).thenReturn(testUserResponse);

            UserResponse result = userService.createUser(request);

            assertThat(result).isNotNull();
            assertThat(result.username()).isEqualTo("mario.rossi");
            verify(eventPublisher).publishUserCreated(testUser);
        }

        @Test
        @DisplayName("throws DuplicateResourceException when email already exists")
        void throwsOnDuplicateEmail() {
            CreateUserRequest request = new CreateUserRequest(
                "mario.rossi", "mario.rossi@example.com", "RSSMRA85M01H501Z",
                "Mario", "Rossi", Set.of(ApplicationRole.DEVELOPER)
            );

            when(userRepository.existsByEmailIgnoreCase("mario.rossi@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");

            verify(userRepository, never()).save(any());
            verify(eventPublisher, never()).publishUserCreated(any());
        }

        @Test
        @DisplayName("throws DuplicateResourceException when CF already exists")
        void throwsOnDuplicateCF() {
            CreateUserRequest request = new CreateUserRequest(
                "mario.rossi", "mario.rossi@example.com", "RSSMRA85M01H501Z",
                "Mario", "Rossi", Set.of(ApplicationRole.DEVELOPER)
            );

            when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
            when(userRepository.existsActiveByCodiceFiscale("RSSMRA85M01H501Z")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("codiceFiscale");
        }
    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {

        @Test
        @DisplayName("returns user when found")
        void returnsUser() {
            when(userRepository.findActiveById(userId)).thenReturn(Optional.of(testUser));
            when(userMapper.toResponse(testUser)).thenReturn(testUserResponse);

            UserResponse result = userService.getUserById(userId, false);

            assertThat(result.id()).isEqualTo(userId);
        }

        @Test
        @DisplayName("throws UserNotFoundException when not found")
        void throwsWhenNotFound() {
            when(userRepository.findActiveById(userId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserById(userId, false))
                .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("disableUser")
    class DisableUser {

        @Test
        @DisplayName("disables an active user")
        void disablesActiveUser() {
            when(userRepository.findActiveById(userId)).thenReturn(Optional.of(testUser));
            when(userRepository.save(testUser)).thenReturn(testUser);
            when(userMapper.toResponse(testUser)).thenReturn(testUserResponse);

            userService.disableUser(userId);

            assertThat(testUser.getStatus()).isEqualTo(UserStatus.DISABLED);
            verify(userRepository).save(testUser);
        }

        @Test
        @DisplayName("is idempotent — no-op if already DISABLED")
        void idempotentOnAlreadyDisabled() {
            testUser.setStatus(UserStatus.DISABLED);
            when(userRepository.findActiveById(userId)).thenReturn(Optional.of(testUser));
            when(userMapper.toResponse(testUser)).thenReturn(testUserResponse);

            userService.disableUser(userId);

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {

        @Test
        @DisplayName("soft-deletes an active user")
        void softDeletesUser() {
            when(userRepository.findActiveById(userId)).thenReturn(Optional.of(testUser));
            when(userRepository.save(testUser)).thenReturn(testUser);

            userService.deleteUser(userId);

            assertThat(testUser.getStatus()).isEqualTo(UserStatus.DELETED);
        }
    }
}
