package org.stapledon.data.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.EnumSet;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(of={ "firstName", "lastName"})
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private EnumSet<Role> roles;
}
