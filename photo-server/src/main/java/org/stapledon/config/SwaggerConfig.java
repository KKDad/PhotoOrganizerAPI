package org.stapledon.config;

import io.swagger.annotations.SecurityDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI photoOrganizerOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(securityRequirement())
                .components(createJwtSecurityScheme())
                .info(new Info().title("PhotoOrganizer API")
                        .description("PhotoOrganizer image organizer")
                        .version("v0.0.1")
                        .license(new License().name("MIT").url("https://github.com/KKDad/PhotoOrganizerAPI/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("PhotoOrganizer Documentation")
                        .url("https://github.com/KKDad/PhotoOrganizerAPI/docs"));

    }

    public SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList("bearerAuth", "bearer-token")
                .addList("apiKey", "api-key");
    }

    private Components createJwtSecurityScheme() {
        return new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .name("bearer-token")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .description("JWT Authentication"))

                .addSecuritySchemes("apiKey", new SecurityScheme()
                        .name("api-key")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .description("API Key Authentication")
        );
    }
}