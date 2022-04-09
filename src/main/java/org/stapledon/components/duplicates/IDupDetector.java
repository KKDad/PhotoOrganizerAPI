package org.stapledon.components.duplicates;

import org.stapledon.dto.Photo;

public interface IDupDetector {
    boolean isDuplicate(Photo left, Photo right) throws CannotDetermineException;
}
