package org.stapledon.components.organizers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stapledon.dto.Photo;

import java.nio.file.Path;
import java.time.format.DateTimeFormatterBuilder;


@Component
@Slf4j
public class FolderYearMonthDayOrganizer implements IOrganizer {

    @Value("${organizer.base-path}")
    Path destinationBasePath;

    public Path choose(Photo photo) {
        if (photo.getTakeOutDetails() == null || photo.getTakeOutDetails().creationTime == null)
            return null;
        var taken = photo.getTakeOutDetails().creationTime;

        var formatter = new DateTimeFormatterBuilder().appendPattern("yyyy/yyyy-MM-dd").toFormatter();
        return Path.of(destinationBasePath.toString(), taken.toLocalDateTime().format(formatter));
    }
}
