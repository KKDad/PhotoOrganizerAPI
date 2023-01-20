package org.stapledon.configuration.properties;

import lombok.Data;

import java.nio.file.Path;

@Data
public class TakeoutProperties {
    private Path exports;
}
