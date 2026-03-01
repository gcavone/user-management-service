package com.intesi.ums.mapper;

import com.intesi.ums.domain.ApplicationRole;
import com.intesi.ums.domain.User;
import com.intesi.ums.dto.CreateUserRequest;
import com.intesi.ums.dto.UpdateUserRequest;
import com.intesi.ums.dto.UserResponse;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-01T16:25:34+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.username( user.getUsername() );
        userResponse.email( user.getEmail() );
        userResponse.codiceFiscale( user.getCodiceFiscale() );
        userResponse.nome( user.getNome() );
        userResponse.cognome( user.getCognome() );
        userResponse.status( user.getStatus() );
        Set<ApplicationRole> set = user.getRoles();
        if ( set != null ) {
            userResponse.roles( new LinkedHashSet<ApplicationRole>( set ) );
        }
        userResponse.createdAt( user.getCreatedAt() );
        userResponse.updatedAt( user.getUpdatedAt() );
        userResponse.createdBy( user.getCreatedBy() );

        return userResponse.build();
    }

    @Override
    public UserResponse toMaskedResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.username( user.getUsername() );
        userResponse.nome( user.getNome() );
        userResponse.cognome( user.getCognome() );
        userResponse.status( user.getStatus() );
        Set<ApplicationRole> set = user.getRoles();
        if ( set != null ) {
            userResponse.roles( new LinkedHashSet<ApplicationRole>( set ) );
        }
        userResponse.createdAt( user.getCreatedAt() );
        userResponse.updatedAt( user.getUpdatedAt() );
        userResponse.createdBy( user.getCreatedBy() );

        userResponse.email( com.intesi.ums.dto.UserResponse.maskEmail(user.getEmail()) );
        userResponse.codiceFiscale( com.intesi.ums.dto.UserResponse.maskCodiceFiscale(user.getCodiceFiscale()) );

        return userResponse.build();
    }

    @Override
    public User toEntity(CreateUserRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( request.username() );
        user.email( request.email() );
        user.codiceFiscale( request.codiceFiscale() );
        user.nome( request.nome() );
        user.cognome( request.cognome() );
        Set<ApplicationRole> set = request.roles();
        if ( set != null ) {
            user.roles( new LinkedHashSet<ApplicationRole>( set ) );
        }

        return user.build();
    }

    @Override
    public void updateEntityFromRequest(User user, UpdateUserRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.username() != null ) {
            user.setUsername( request.username() );
        }
        if ( request.nome() != null ) {
            user.setNome( request.nome() );
        }
        if ( request.cognome() != null ) {
            user.setCognome( request.cognome() );
        }
        if ( user.getRoles() != null ) {
            Set<ApplicationRole> set = request.roles();
            if ( set != null ) {
                user.getRoles().clear();
                user.getRoles().addAll( set );
            }
        }
        else {
            Set<ApplicationRole> set = request.roles();
            if ( set != null ) {
                user.setRoles( new LinkedHashSet<ApplicationRole>( set ) );
            }
        }
    }
}
