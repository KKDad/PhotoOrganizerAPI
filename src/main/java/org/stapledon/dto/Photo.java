package org.stapledon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.vision.VisionDetails;

import java.nio.file.Path;

@Data
@Jacksonized
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    String name;

    Path basePath;

    Path imagePath;

    Path takeOutDetailsPath;

    Path visionDetailsPath;

    PhotoDetails takeOutDetails;

    VisionDetails visionDetails;

    @JsonIgnore
    @Builder.Default
    boolean takeOutDetailsModified = false;

    @JsonIgnore
    @Builder.Default
    boolean visionDetailsModified = false;
}
