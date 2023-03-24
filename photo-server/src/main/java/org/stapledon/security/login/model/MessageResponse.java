package org.stapledon.security.login.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class MessageResponse {
    private String message;
}
