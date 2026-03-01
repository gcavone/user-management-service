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
     * Find all non-deleted users with optional filtering.
     * Using JPQL instead of derived methods for readability and performance control.
     */
    @Query("""
        SELECT u FROM User u
        WHERE u.status != 'DELETED'
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

    boolean existsByEmailIgnoreCase(String email);

    /**
     * Check CF uniqueness only among non-deleted users.
     * Allows reassigning a CF if the original holder was soft-deleted.
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE UPPER(u.codiceFiscale) = UPPER(:cf) AND u.status != 'DELETED'")
    boolean existsActiveByCodiceFiscale(@Param("cf") String codiceFiscale);

    boolean existsByUsernameIgnoreCase(String username);
}
