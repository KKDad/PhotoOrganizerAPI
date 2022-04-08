package org.stapledon.service;

import java.io.IOException;

public class TidyUpException extends RuntimeException {
    public TidyUpException(String localizedMessage, IOException ie) {
        super(localizedMessage, ie);
    }

    public TidyUpException(String localizedMessage) {
        super(localizedMessage);
    }
}
