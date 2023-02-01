package org.stapledon.components.organizers;

import org.stapledon.dto.Photo;

import java.nio.file.Path;

public interface IOrganizer {

    /**
     * Determine the correct path for the photo given the photo's metadata
     * @param photo - Photo to Organize
     * @return - Destination Path
     */
    Path choosePath(Photo photo);

    /**
     * Determine the correct filename for the photo given the photo's metadata
     * @param photo - Photo to Rename
     * @return - New Name
     */
    Path chooseName(Photo photo);
}
