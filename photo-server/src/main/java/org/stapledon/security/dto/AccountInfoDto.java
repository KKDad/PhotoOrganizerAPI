package org.stapledon.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class AccountInfoDto {
    @NonNull
    @Schema(description = "Email address",
            example = "john@stapledon.local")
    private String email;

    @Schema(description = "Password. Only used when creating or updating a user. Never returned from the server. Leave blank when updating a user to keep the existing password.",
            example = "password")
    private String password;

    @NonNull
    @Schema(description = "Username used to log in",
            example = "kkdad")
    private String username;

    @Schema(description = "users firstname",
            nullable = true,
            example = "John")
    private String firstName;
    @Schema(description = "users surname",
            nullable = true,
            example = "Doe")
    private String lastName;
    @NonNull
    @Schema(description = "Assigned Roles")
    @Builder.Default
    private Set<String> roles = Set.of();
}
