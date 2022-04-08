package org.stapledon.components.organizers;

import org.stapledon.dto.Photo;

import java.nio.file.Path;

public interface IOrganizer {
    Path choose(Photo photo);
}
