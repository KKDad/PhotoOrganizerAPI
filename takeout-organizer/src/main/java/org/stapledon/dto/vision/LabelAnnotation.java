package org.stapledon.dto.vision;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class LabelAnnotation {
    String mid;
    String description;
    Float score;
    Float topicality;
}
