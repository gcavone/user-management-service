package com.intesi.ums.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("User Management Service API")
                .description("""
                    Enterprise-grade User Management Service.
                    
                    **Authentication**: Bearer JWT issued by Keycloak.
                    
                    **Roles**:
                    - `OWNER` — full access: create, read, update, disable, delete. Sees unmasked data.
                    - `OPERATOR` — create, read, update, disable. Sees unmasked data.
                    - `MAINTAINER` — create, read, update. Sees unmasked data.
                    - `DEVELOPER` — read-only. Sees unmasked data.
                    - `REPORTER` — read-only. Email and CF are masked.
                    """)
                .version("v1.0.0")
                .contact(new Contact().name("Giuseppe Cavone").email("giuseppe@cavone.net")))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("Local development")
            ))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token obtained from Keycloak")));
    }
}