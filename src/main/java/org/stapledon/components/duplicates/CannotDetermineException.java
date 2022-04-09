package org.stapledon.components.duplicates;

import java.io.IOException;

public class CannotDetermineException extends Exception {
    public CannotDetermineException(String message, IOException e) {
        super(message, e);
    }
}
