package org.stapledon.data.entities;

import lombok.*;

import javax.persistence.*;
import java.util.EnumSet;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
@ToString(of={ "firstName", "lastName"})
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private EnumSet<Role> roles;
}
