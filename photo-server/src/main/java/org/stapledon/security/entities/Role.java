package org.stapledon.security.entities;

import jakarta.persistence.*;
import lombok.*;
import org.stapledon.security.entities.enums.UserRole;

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
    private UserRole roleName;
}
