package org.stapledon.dto.vision;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DominantColors {
    Float red;
    Float green;
    Float blue;
    Float score;
    Float pixelFraction;
}

