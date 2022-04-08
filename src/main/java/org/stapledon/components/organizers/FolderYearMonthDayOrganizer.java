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
        var taken = photo.getTakeOutDetails().creationTime;
        if (taken == null)
            return null;

        var formatter = new DateTimeFormatterBuilder().appendPattern("yyyy/yyyy-MM-dd").toFormatter();
        return Path.of(destinationBasePath.toString(), taken.toLocalDateTime().format(formatter));
    }
}
