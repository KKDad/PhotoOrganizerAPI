package org.stapledon.security.dto;

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
