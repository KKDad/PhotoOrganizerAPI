package org.stapledon.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.stapledon.dto.takeout.PhotoDetails;
import org.stapledon.dto.vision.VisionDetails;

import java.nio.file.Path;
import java.util.List;

@SuperBuilder
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Photo {

    String name;

    List<Path> pathList;

    PhotoDetails takeOutDetails;

    VisionDetails visionDetails;

    public Path imagePath() {
        return pathList.stream().filter(p -> p.toString().endsWith(".jpg")).findFirst().orElse(null);
    }
}
