package org.stapledon.security.model;

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
