package org.stapledon.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI photoOrganizerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("PhotoOrganizer API")
                        .description("PhotoOrganizer image organizer")
                        .version("v0.0.1")
                        .license(new License().name("MIT").url("https://github.com/KKDad/PhotoOrganizerAPI/LICENSE")))
                .externalDocs(new ExternalDocumentation()
                        .description("PhotoOrganizer Documentation")
                        .url("https://github.com/KKDad/PhotoOrganizerAPI/docs"));
    }
}