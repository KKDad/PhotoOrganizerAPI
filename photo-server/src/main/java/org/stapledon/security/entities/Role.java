package org.stapledon.security.entities;

import jakarta.persistence.*;
import lombok.*;
import org.stapledon.security.entities.enums.AccountRole;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AccountRole roleName;
}
