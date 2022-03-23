package org.stapledon.dto.vision;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


@Data
@Builder
@Jacksonized
public class DominantColors {
    Float red;
    Float green;
    Float blue;
    Float score;
    Float pixelFraction;
}

