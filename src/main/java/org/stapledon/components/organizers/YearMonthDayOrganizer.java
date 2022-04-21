package org.stapledon.components.organizers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stapledon.configuration.properties.OrganizerProperties;
import org.stapledon.dto.Photo;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;


@Component
@Slf4j
public class YearMonthDayOrganizer extends Organizer {

    @Autowired
    OrganizerProperties config;

    @Override
    public Path choosePath(Photo photo) {
        LocalDateTime taken = getPhotoTakenTime(photo);
        if (taken == null)
            return null;

        var formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
        return Path.of(config.getDestination().getBasePath(), taken.format(formatter));
    }
}
