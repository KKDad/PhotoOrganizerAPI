package org.stapledon.security.login.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
