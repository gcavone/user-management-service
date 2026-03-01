package com.intesi.ums.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intesi.ums.domain.ApplicationRole;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.UpdateUserRequest;
import com.intesi.ums.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests using Testcontainers for real PostgreSQL and RabbitMQ instances.
 *
 * These tests exercise the full request -> controller -> service -> repository stack.
 * Spring Security is mocked via @WithMockUser to isolate auth from business logic tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User API Integration Tests")
class UserApiIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
        .withDatabaseName("ums_test")
        .withUsername("ums_user")
        .withPassword("ums_password");

    @Container
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3.12-management-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.rabbitmq.host", rabbitmq::getHost);
        registry.add("spring.rabbitmq.port", rabbitmq::getAmqpPort);
        // Disable JWT validation in tests
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
            () -> "http://localhost:9999/realms/test/protocol/openid-connect/certs");
    }

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;

    private static String createdUserId;

    @BeforeEach
    void cleanUp() {
        if (createdUserId == null) {
            userRepository.deleteAll();
        }
    }

    // ---- CREATE ----

    @Test
    @Order(1)
    @WithMockUser(roles = "MAINTAINER")
    @DisplayName("POST /users - creates a user and returns 201")
    void createUser_returns201() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
            "mario.rossi", "mario.rossi@example.com", "RSSMRA80A01H501U",
            "Mario", "Rossi", Set.of(ApplicationRole.DEVELOPER)
        );

        String responseBody = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.username").value("mario.rossi"))
            .andExpect(jsonPath("$.email").value("mario.rossi@example.com"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.roles", hasItem("DEVELOPER")))
            .andReturn().getResponse().getContentAsString();

        createdUserId = objectMapper.readTree(responseBody).get("id").asText();
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "MAINTAINER")
    @DisplayName("POST /users - returns 409 on duplicate email")
    void createUser_returns409OnDuplicateEmail() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
            "another.user", "mario.rossi@example.com", "VRDGNN80A01H501J",
            "Giovanni", "Verdi", Set.of(ApplicationRole.REPORTER)
        );

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("DUPLICATE_RESOURCE"));
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "MAINTAINER")
    @DisplayName("POST /users - returns 400 on invalid Codice Fiscale")
    void createUser_returns400OnInvalidCF() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
            "test.user", "test@example.com", "INVALIDCF123456",
            "Test", "User", Set.of(ApplicationRole.DEVELOPER)
        );

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors[?(@.field=='codiceFiscale')]").isNotEmpty());
    }

    // ---- READ ----

    @Test
    @Order(4)
    @WithMockUser(roles = "OWNER")
    @DisplayName("GET /users/{id} - returns full user data for ADMIN")
    void getUser_adminSeesUnmaskedData() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + createdUserId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("mario.rossi@example.com"))
            .andExpect(jsonPath("$.codiceFiscale").value("RSSMRA80A01H501U"));
    }

    @Test
    @Order(5)
    @WithMockUser(roles = "REPORTER")
    @DisplayName("GET /users/{id} - VIEWER receives masked email and CF")
    void getUser_viewerSeesMaskedData() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + createdUserId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(containsString("***")))
            .andExpect(jsonPath("$.codiceFiscale").value(containsString("*")));
    }

    @Test
    @Order(6)
    @WithMockUser(roles = "OWNER")
    @DisplayName("GET /users/{id} - returns 404 for non-existent user")
    void getUser_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/users/00000000-0000-0000-0000-000000000000"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("USER_NOT_FOUND"));
    }

    // ---- LIST ----

    @Test
    @Order(7)
    @WithMockUser(roles = "OWNER")
    @DisplayName("GET /users - returns paginated list")
    void listUsers_returnsPaginatedList() throws Exception {
        mockMvc.perform(get("/api/v1/users?page=0&size=10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.totalElements").value(greaterThanOrEqualTo(1)))
            .andExpect(jsonPath("$.page").value(0));
    }

    // ---- UPDATE ----

    @Test
    @Order(8)
    @WithMockUser(roles = "MAINTAINER")
    @DisplayName("PATCH /users/{id} - updates nome and roles")
    void updateUser_updatesSuccessfully() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest(
            null, "Mario Updated", null, Set.of(ApplicationRole.MAINTAINER)
        );

        mockMvc.perform(patch("/api/v1/users/" + createdUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Mario Updated"))
            .andExpect(jsonPath("$.roles", hasItem("MAINTAINER")));
    }

    // ---- DISABLE / DELETE ----

    @Test
    @Order(9)
    @WithMockUser(roles = "OWNER")
    @DisplayName("PATCH /users/{id}/disable - disables user")
    void disableUser() throws Exception {
        mockMvc.perform(patch("/api/v1/users/" + createdUserId + "/disable"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DISABLED"));
    }

    @Test
    @Order(10)
    @WithMockUser(roles = "OWNER")
    @DisplayName("DELETE /users/{id} - soft-deletes user, returns 204")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + createdUserId))
            .andExpect(status().isNoContent());

        // User should no longer be findable
        mockMvc.perform(get("/api/v1/users/" + createdUserId))
            .andExpect(status().isNotFound());
    }

    // ---- SECURITY ----

    @Test
    @Order(11)
    @WithMockUser(roles = "REPORTER")
    @DisplayName("POST /users - VIEWER cannot create users, returns 403")
    void createUser_viewerForbidden() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
            "test.viewer", "viewer@example.com", "VRDGNN80A01H501J",
            "Test", "Viewer", Set.of(ApplicationRole.REPORTER)
        );

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Order(12)
    @DisplayName("GET /users - unauthenticated request returns 401")
    void listUsers_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isUnauthorized());
    }
}