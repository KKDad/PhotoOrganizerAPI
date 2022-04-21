package org.stapledon.components.organizers;

import org.stapledon.dto.Photo;

import java.nio.file.Path;
import java.time.LocalDateTime;

public abstract class  Organizer implements IOrganizer {
    protected LocalDateTime getPhotoTakenTime(Photo photo) {
        if (photo.getTakeOutDetails() == null)
            return null;
        var taken = photo.getTakeOutDetails().getPhotoTakenTime() == null ?
                photo.getTakeOutDetails().getCreationTime() :
                photo.getTakeOutDetails().getPhotoTakenTime();

        if (taken == null)
            return null;

        return taken.toLocalDateTime();
    }

    @Override
    public Path chooseName(Photo photo) {
        return null;
    }
}
