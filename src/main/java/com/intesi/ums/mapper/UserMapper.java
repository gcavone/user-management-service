package com.intesi.ums.mapper;

import com.intesi.ums.domain.User;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.UserResponse;
import org.mapstruct.*;

/**
 * MapStruct mapper for User <-> DTO conversions.
 *
 * Two mapping methods are provided:
 * - toResponse()        : full data (for OWNER, OPERATOR, MAINTAINER, DEVELOPER roles)
 * - toMaskedResponse()  : sensitive fields masked (for REPORTER role)
 *
 * Using componentModel = "spring" so the mapper is injected as a Spring bean.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    /**
     * Full response — all fields visible.
     */
    @Mapping(target = "id", source = "id")
    UserResponse toResponse(User user);

    /**
     * Masked response — sensitive fields replaced.
     */
    @Mapping(target = "email", expression = "java(com.intesi.ums.dto.UserResponse.maskEmail(user.getEmail()))")
    @Mapping(target = "codiceFiscale", expression = "java(com.intesi.ums.dto.UserResponse.maskCodiceFiscale(user.getCodiceFiscale()))")
    UserResponse toMaskedResponse(User user);

    /**
     * Maps creation request to a new User entity.
     * id, status, audit fields are managed by JPA / application logic.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    User toEntity(CreateUserRequest request);

    /**
     * Applies partial update to an existing entity (IGNORE null fields).
     * email and codiceFiscale are excluded from updates by design.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "codiceFiscale", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromRequest(
        @MappingTarget User user,
        com.intesi.ums.dto.UpdateUserRequest request
    );
}