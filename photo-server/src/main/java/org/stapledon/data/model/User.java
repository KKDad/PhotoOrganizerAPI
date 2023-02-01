package org.stapledon.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.stapledon.data.domain.Role;

import java.util.Set;

@Getter
@Setter
@ToString
public class User {
    @NonNull
    @Schema(description = "Email address, used to log in",
            example = "john@stapledon.local")
    private String email;
    @Schema(description = "User's firstname",
            nullable = true,
            example = "John")

    private String firstName;
    @Schema(description = "User's surname",
            nullable = true,
            example = "Doe")
    private String lastName;
    @NonNull
    @Schema(description = "Assigned Roles")
    private Set<Role> roles;
}
