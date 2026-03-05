package com.intesi.ums.repository;

import com.intesi.ums.domain.User;
import com.intesi.ums.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find users with optional filtering.
     * When status=DELETED is explicitly passed, deleted users are included.
     * When no status filter is provided, deleted users are excluded by default.
     */
    @Query("""
        SELECT u FROM User u
        WHERE (:status IS NOT NULL OR u.status != 'DELETED')
        AND (:status IS NULL OR u.status = :status)
        AND (:search IS NULL
             OR LOWER(u.username) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
             OR LOWER(u.email) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
             OR LOWER(u.nome) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
             OR LOWER(u.cognome) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))
        """)
    Page<User> findAllActive(
        @Param("status") UserStatus status,
        @Param("search") String search,
        Pageable pageable
    );

    /**
     * Find a single non-deleted user by ID.
     */
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.status != 'DELETED'")
    Optional<User> findActiveById(@Param("id") UUID id);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(:email) AND u.status != 'DELETED'")
    boolean existsActiveByEmailIgnoreCase(@Param("email") String email);

    /**
     * Check CF uniqueness among ALL users including deleted ones.
     * In a regulated QTSP context, a CF identifies a natural person by law —
     * reusing it even after soft-delete could create ambiguity in compliance audits.
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE UPPER(u.codiceFiscale) = UPPER(:cf)")
    boolean existsByCodiceFiscale(@Param("cf") String codiceFiscale);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.username) = LOWER(:username) AND u.status != 'DELETED'")
    boolean existsActiveByUsernameIgnoreCase(@Param("username") String username);
}