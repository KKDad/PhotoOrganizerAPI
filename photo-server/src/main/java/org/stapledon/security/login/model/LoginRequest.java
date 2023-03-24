package org.stapledon.security.login.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

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
