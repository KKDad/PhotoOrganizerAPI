package org.stapledon.dto.vision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisionDetails {

    boolean processed;

    LocalDateTime resultDate;

    @Builder.Default
    List<LabelAnnotation> labels = new ArrayList<>();

    @Builder.Default
    List<DominantColors> colors = new ArrayList<>();

}
