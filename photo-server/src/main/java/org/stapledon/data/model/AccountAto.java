package org.stapledon.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class AccountAto {
    @NonNull
    @Schema(description = "Email address",
            example = "john@stapledon.local")
    private String email;

    @NonNull
    @Schema(description = "Password",
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
    private Set<RoleAto> roles;
}
