# User Management Service

Enterprise-grade microservice for user lifecycle management, built with Java 21 + Spring Boot 3.2.

---

## Quick Start

### Prerequisites
- Docker + Docker Compose

### Start the full stack

```bash
git clone <repo-url>
cd user-management-service
docker-compose up -d
```

All four services will start automatically:

| Service | URL | Credentials |
|---------|-----|-------------|
| Swagger UI | http://localhost:8080/swagger-ui.html | — |
| Health check | http://localhost:8080/actuator/health | — |
| Keycloak (IAM) | http://localhost:9090 | admin / admin |
| RabbitMQ Management | http://localhost:15672 | guest / guest |

> **Note:** Keycloak takes 2-3 minutes to fully start on first launch. Wait until `http://localhost:8080/actuator/health` returns `{"status":"UP"}` before testing the APIs.

---

## Testing the APIs

The recommended way to test the APIs is via **Swagger UI** at http://localhost:8080/swagger-ui.html.

All endpoints require a valid JWT token issued by Keycloak. Follow these steps:

### Step 1 — Obtain a token from Keycloak

Two scripts are provided depending on your OS. Both print the token directly — copy it and paste it in Swagger UI.

**macOS / Linux / Git Bash (Windows):**
```bash
./get-token.sh <username> <password>
```

**Windows PowerShell:**

> **Note for Windows users:** PowerShell blocks script execution by default. Run this command once before using the script:
> ```powershell
> Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
> ```

```powershell
.\get-token.ps1 <username> <password>
```

| User | Password | Role |
|------|----------|------|
| owner-user | owner123 | OWNER |
| operator-user | operator123 | OPERATOR |
| maintainer-user | maintainer123 | MAINTAINER |
| developer-user | developer123 | DEVELOPER |
| reporter-user | reporter123 | REPORTER |

Example:
```bash
./get-token.sh owner-user owner123
```

> **Note:** Tokens expire after 30 minutes. If you receive a `401 Unauthorized`, repeat this step to obtain a fresh token.

### Step 2 — Authorize in Swagger UI

1. Open http://localhost:8080/swagger-ui.html
2. Click the **Authorize** button (🔓 top right)
3. Paste the `access_token` value in the `bearerAuth` field
4. Click **Authorize**, then **Close**

All subsequent requests will automatically include the token.

---

## API Reference

Full interactive documentation: **http://localhost:8080/swagger-ui.html**

| Method | Path | Minimum role | Description |
|--------|------|-------------|-------------|
| GET | `/api/v1/users` | REPORTER | List users (paginated + filterable) |
| GET | `/api/v1/users/{id}` | REPORTER | Get user by ID |
| POST | `/api/v1/users` | MAINTAINER | Create user |
| PATCH | `/api/v1/users/{id}` | MAINTAINER | Update user data/roles |
| PATCH | `/api/v1/users/{id}/disable` | OPERATOR | Disable user |
| DELETE | `/api/v1/users/{id}` | OWNER | Soft-delete user |

### Role hierarchy

| Role | Endpoints | Sensitive data (email, CF) |
|------|-----------|---------------------------|
| OWNER | Full access: create, read, update, disable, delete. Can filter users by `status=DELETED` | Visible |
| OPERATOR | Create, read, update, disable | Visible |
| MAINTAINER | Create, read, update | Visible |
| DEVELOPER | Read-only | Visible |
| REPORTER | Read-only | Masked |

### Query Parameters for List

| Parameter | Type | Description |
|-----------|------|-------------|
| `status` | `ACTIVE` \| `DISABLED` \| `DELETED` | Filter by status |
| `search` | string | Free-text search on username, email, nome, cognome |
| `page` | int (default: 0) | Page number (0-indexed) |
| `size` | int (default: 20, max: 100) | Page size |
| `sortBy` | string (default: `createdAt`) | Sort field (any User field: id, username, email, nome, cognome, status, createdAt, updatedAt) |
| `direction` | `ASC` \| `DESC` (default: `DESC`) | Sort direction |

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    user-management-service                      │
│                                                                 │
│  ┌──────────────┐   ┌──────────────┐   ┌──────────────────┐   │
│  │ Controller   │ → │   Service    │ → │   Repository     │   │
│  │ (REST/HTTP)  │   │ (Business    │   │ (Spring Data JPA)│   │
│  │              │   │  Logic)      │   │                  │   │
│  └──────────────┘   └──────────────┘   └──────────────────┘   │
│         │                  │                      │             │
│    DTOs + Validation   Events (async)        PostgreSQL        │
│         │                  │                                   │
│    GlobalException     RabbitMQ                                │
│    Handler                                                     │
└─────────────────────────────────────────────────────────────────┘
         │                  │
    Keycloak (IAM)     ums.events exchange
    JWT validation     → ums.user.created queue
```

**Layer responsibilities:**

- **Controller** — HTTP concerns only: routing, request/response mapping, auth annotations, Swagger docs
- **Service** — all business logic: validation, state transitions, event publishing
- **Repository** — data access: custom JPQL queries, pagination, soft-delete filtering
- **Mapper** — explicit DTO ↔ Entity conversion via MapStruct (no implicit field leakage)

---

## Technical Choices & Trade-offs

### Database: PostgreSQL

**Why PostgreSQL over MongoDB:**

The domain has clear relational structure (users, roles) with strict constraints (unique email, unique CF, referential integrity on user_roles). PostgreSQL gives:
- **ACID transactions** — critical when checking uniqueness and inserting atomically
- **Partial unique indexes** — `uq_users_email_active`, `uq_users_cf_active` and `uq_users_username_active` enforce uniqueness only among non-deleted users, avoiding conflicts with soft-deleted records
- **UUID PK** — distributed-system ready; no coordination required for ID generation
- Native `gen_random_uuid()` for zero-dependency UUID generation

MongoDB would have been a better choice if the user schema was highly variable or document-oriented. It is not.

### Identity & Access Management: Keycloak

The service acts as an **OAuth2 Resource Server** — it trusts and validates JWTs issued by Keycloak but never issues tokens itself. This follows the standard enterprise microservice pattern:

- Auth logic stays in Keycloak, not in application code
- JWTs carry Keycloak realm roles (`OWNER`, `OPERATOR`, `MAINTAINER`, `DEVELOPER`, `REPORTER`) in the `realm_access.roles` claim
- A custom `JwtAuthenticationConverter` maps these to Spring Security `GrantedAuthority` objects
- `@PreAuthorize` annotations enforce RBAC at the method level

The same five roles defined in the domain govern both API access and data visibility — there is no separate set of security roles.

### Async Events: RabbitMQ with Topic Exchange

When a user is created, a `UserCreatedEvent` is published to the `ums.events` topic exchange with routing key `user.created`. Downstream services (notification, audit, Keycloak sync) subscribe independently.

**Why topic exchange over direct:**
- Wildcards (`user.#`) allow consumers to subscribe to all future user events without topology changes
- Adding `user.updated`, `user.deleted` events in the future requires no changes to the exchange

**Dead-letter queue (`ums.user.created.dlq`):** failed messages are routed here for inspection rather than being lost.

**Resilience design:** event publishing failure does NOT roll back user creation. The user record is always saved first. In production this pattern would be complemented by the [Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html) for guaranteed delivery.

### Schema Management: Flyway

Flyway owns all DDL. Hibernate is configured with `ddl-auto: validate` — it only validates the schema, never modifies it. This is the correct production approach:
- Migrations are versioned and repeatable
- Schema changes are tracked in VCS
- Safe for blue/green deployments

### Soft Delete

Users are never physically deleted. `DELETE /users/{id}` sets `status = 'DELETED'`. Reasons:
- Audit trail preserved
- Foreign key references remain valid
- Reversible by ops team directly in DB if needed

Deleted users are excluded from all queries by default. Only OWNER can retrieve them explicitly via `GET /api/v1/users?status=DELETED`.

### Audit Trail: DB Trigger + Spring Data Auditing

`updated_at` is managed by **both** Spring Data Auditing (`@LastModifiedDate`) and a PostgreSQL trigger (`trg_users_updated_at`). This is intentional.

Spring Data Auditing handles `createdAt`, `updatedAt`, `createdBy`, and `updatedBy` for all operations that go through the application. However, in regulated environments it is common for DBAs, batch migration scripts, or compliance tools to operate directly on the database, bypassing the application entirely. In those cases Spring Data Auditing is not active, and `updated_at` would remain stale without an additional safeguard.

The trigger acts as a safety net at the database level:

```sql
CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
```

In case of conflict, the trigger always wins (it fires `BEFORE UPDATE` and overwrites whatever the application set). This guarantees that `updated_at` reflects the actual time of the last modification regardless of how the row was changed.

`created_at` does not need a trigger — it is written once at INSERT time and never changes. The `DEFAULT NOW()` on the column already covers direct INSERT scenarios.

This design is consistent with operating in a QTSP/eIDAS-regulated context where audit trail integrity must be guaranteed at the infrastructure level, not just at the application level.

### Correlation ID

Every request receives an `X-Correlation-ID` header (generated if not provided by the caller). It is stored in the MDC and included in all log lines for that request. This enables end-to-end request tracing across services and is essential in a microservices architecture.

---

## Build & Run

### Prerequisites
- Docker + Docker Compose
- Java 21 + Maven (only for local development without Docker)

### Full stack with Docker
```bash
docker-compose up -d          # Start all services
docker-compose logs -f app    # Follow application logs
docker-compose down           # Stop all services
```

### Local development (without Docker app container)
```bash
# Start only infrastructure
docker-compose up -d postgres rabbitmq keycloak

# Run the app
./mvnw spring-boot:run
```

### Tests

No local Java or Maven installation required — everything runs inside Docker.

**Unit tests** — work on any OS without additional configuration:
```bash
docker-compose run --rm test
```

**Integration tests** — use Testcontainers to spin up dedicated PostgreSQL and RabbitMQ containers, completely isolated from the running stack:
```bash
docker-compose run --rm test-integration
```

> **Note for macOS users:** On some macOS configurations with Docker Desktop, Testcontainers may fail with "Could not find a valid Docker environment". If this happens, use the [CI/CD pipeline](#cicd) to run them — GitHub Actions runners use Linux where this works natively without any additional configuration.

Maven dependencies are cached in a named Docker volume (`maven-cache`) so subsequent runs are significantly faster.

---

## CI/CD

The project includes a GitHub Actions pipeline at `.github/workflows/ci.yml` that runs automatically on every push to `main` or `develop` and on every pull request to `main`.

The pipeline has three jobs that run in sequence:

**1. Unit Tests** — runs `mvn test` on every push. Covers service logic and CF validation.

**2. Integration Tests** — runs `mvn verify` using Testcontainers. GitHub Actions runners (`ubuntu-latest`) have Docker pre-installed, so Testcontainers can spin up PostgreSQL and RabbitMQ containers automatically without any additional configuration. This is the recommended way to run integration tests — it avoids Docker-in-Docker limitations on macOS.

**3. Docker Build** — builds the Docker image on every push to `main`, after both test jobs pass. The image is not pushed to a registry in this configuration (set `push: true` and add registry credentials to enable it).

To trigger the pipeline, push to the repository:
```bash
git push origin main
```

Results are visible at `https://github.com/<username>/<repo>/actions`.

---

## Project Structure

```
src/main/java/com/intesi/ums/
├── config/
│   ├── SecurityConfig.java        # OAuth2 Resource Server, Keycloak JWT converter
│   ├── RabbitMQConfig.java        # Exchange, queues, DLQ topology
│   ├── OpenApiConfig.java         # Swagger/OpenAPI metadata
│   ├── AuditConfig.java           # Spring Data Auditing (createdBy/updatedBy)
│   └── CorrelationIdFilter.java   # X-Correlation-ID MDC injection
├── controller/
│   └── UserController.java        # REST endpoints with RBAC annotations
├── domain/
│   ├── User.java                  # JPA entity with auditing
│   ├── UserStatus.java            # ACTIVE | DISABLED | DELETED
│   └── ApplicationRole.java       # OWNER | OPERATOR | MAINTAINER | DEVELOPER | REPORTER
├── dto/
│   ├── CreateUserRequest.java     # Validated creation payload
│   ├── UpdateUserRequest.java     # Partial update payload
│   ├── UserResponse.java          # Response DTO with masking helpers
│   ├── PagedResponse.java         # Generic pagination wrapper
│   └── ErrorResponse.java        # Standardised error structure
├── exception/
│   ├── UserNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── IllegalStateTransitionException.java
│   └── GlobalExceptionHandler.java  # Centralised error mapping
├── mapper/
│   └── UserMapper.java            # MapStruct: full + masked response variants
├── messaging/
│   ├── UserCreatedEvent.java      # Domain event record
│   └── UserEventPublisher.java    # RabbitMQ publisher
├── repository/
│   └── UserRepository.java        # Custom JPQL queries, soft-delete aware
├── service/
│   ├── UserService.java           # Interface
│   └── UserServiceImpl.java       # Business logic implementation
└── validation/
    ├── ValidCodiceFiscale.java    # Custom annotation
    └── CodiceFiscaleValidator.java # Full Italian CF checksum validation
```

---

## Possible Improvements (Production Roadmap)

- **Outbox Pattern** for guaranteed at-least-once event delivery to RabbitMQ
- **Distributed caching** (Redis) for frequently accessed user data
- **Rate limiting** on the POST endpoint to prevent abuse
- **Keycloak user sync** — create a corresponding Keycloak user account on `POST /users`
- **Cursor-based pagination** instead of offset for large datasets
- **Observability** — Micrometer + Prometheus metrics, OpenTelemetry traces
