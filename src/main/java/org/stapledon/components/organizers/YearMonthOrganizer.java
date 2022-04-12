package org.stapledon.components.organizers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stapledon.dto.Photo;

import java.nio.file.Path;
import java.time.format.DateTimeFormatterBuilder;


@Component
@Slf4j
public class YearMonthOrganizer implements IOrganizer {

    @Value("${organizer.base-path}")
    Path destinationBasePath;

    public Path choosePath(Photo photo) {
        if (photo.getTakeOutDetails() == null)
            return null;
        var taken = photo.getTakeOutDetails().getPhotoTakenTime() == null ?
                photo.getTakeOutDetails().getCreationTime() :
                photo.getTakeOutDetails().getPhotoTakenTime();

        if (taken == null)
            return null;

        var formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM").toFormatter();
        return Path.of(destinationBasePath.toString(), taken.toLocalDateTime().format(formatter));
    }
}
