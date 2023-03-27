package org.stapledon.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.stapledon.data.entities.enums.AccountRole;

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
