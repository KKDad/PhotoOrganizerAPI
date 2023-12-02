package org.stapledon.security.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class AuthRequest {
    private String username;
    private String password;
}
