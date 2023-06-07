package org.stapledon.security.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class UserInfoResponse {
    private Integer id;
    private String username;
    private String email;
    private List<String> roles;
}
