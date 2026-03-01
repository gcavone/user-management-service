package com.intesi.ums.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Core domain entity representing a user in the system.
 *
 * Design decisions:
 * - UUID as PK for distributed-system readiness (no coordination needed for ID generation)
 * - email is unique and NOT updatable (immutable by business rule)
 * - codiceFiscale is unique with a partial index on non-DELETED users (see migration)
 * - roles stored as @ElementCollection in a join table (simple, no Role entity needed)
 * - soft delete via status=DELETED instead of physical removal (audit trail)
 * - Spring Data Auditing for createdAt/updatedAt/createdBy/updatedBy
 */
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_username", columnList = "username"),
        @Index(name = "idx_users_status", columnList = "status"),
        @Index(name = "idx_users_codice_fiscale", columnList = "codice_fiscale")
    }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "roles")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Email is immutable after creation as per business rules.
     * updatable = false enforces this at the ORM level.
     */
    @Column(nullable = false, unique = true, length = 255, updatable = false)
    private String email;

    @Column(name = "codice_fiscale", nullable = false, length = 16)
    private String codiceFiscale;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 100)
    private String cognome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    /**
     * Roles stored as an element collection in a join table.
     * This avoids over-engineering a full Role entity while still
     * maintaining clean relational modeling with proper constraints.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        indexes = @Index(name = "idx_user_roles_user_id", columnList = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private Set<ApplicationRole> roles = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // ---- domain methods ----

    public boolean isActive() {
        return UserStatus.ACTIVE == this.status;
    }

    public void disable() {
        this.status = UserStatus.DISABLED;
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }
}
