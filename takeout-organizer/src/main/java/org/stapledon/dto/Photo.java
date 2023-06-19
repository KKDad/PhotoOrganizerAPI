package org.stapledon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.vision.VisionDetails;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    String name;

    String folder;

    Path basePath;

    Path imagePath;

    Path takeOutDetailsPath;

    Path visionDetailsPath;

    @Builder.Default
    List<Path> additionalImages = new ArrayList<>();

    PhotoDetails takeOutDetails;

    VisionDetails visionDetails;

    @JsonIgnore
    @Builder.Default
    boolean takeOutDetailsModified = false;

    @JsonIgnore
    @Builder.Default
    boolean visionDetailsModified = false;
}
