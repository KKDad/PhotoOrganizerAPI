package org.stapledon.configuration.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@RequiredArgsConstructor
public class TakeoutProperties {
    private Path exports;
}
