package org.stapledon.data.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;
import org.stapledon.data.domain.Role;

import java.util.EnumSet;

@Getter
@Setter
@ToString
public class User {
    @NonNull
    public String email;
    public String firstName;
    @ApiModelProperty(notes = "User's surname")
    public String lastName;
    @NonNull
    public EnumSet<Role> roles;
}
