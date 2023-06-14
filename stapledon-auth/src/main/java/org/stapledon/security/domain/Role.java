package org.stapledon.security.domain;

import jakarta.persistence.*;
import lombok.*;
import org.stapledon.security.domain.enums.AccountRole;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AccountRole roleName;
}
