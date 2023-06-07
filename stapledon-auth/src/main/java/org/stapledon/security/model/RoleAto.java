package org.stapledon.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.stapledon.security.domain.enums.AccountRole;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class RoleAto {
    @Schema(description = "role name",
            nullable = true,
            example = "ROLE_USER")
    private AccountRole roleName;
}
