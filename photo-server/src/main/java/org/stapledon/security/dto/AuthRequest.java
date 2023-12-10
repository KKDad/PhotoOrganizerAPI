package org.stapledon.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Schema(description = "Authentication request")
public class AuthRequest {
    private String username;
    private String password;
}
