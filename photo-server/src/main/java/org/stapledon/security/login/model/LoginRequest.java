package org.stapledon.security.login.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
