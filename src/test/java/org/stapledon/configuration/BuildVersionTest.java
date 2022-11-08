package org.stapledon.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class BuildVersionTest {

    @Test
    void getBuildPropertyTest() {
        BuildVersion version = new BuildVersion();

        var props = Arrays.asList("build.artifact", "build.group", "build.name", "build.time", "build.version");

        props.forEach(prop -> {
            String buildProperty = version.getBuildProperty(prop);
            assertNotNull(buildProperty, String.format("Expected %s to be defined", prop));
        });
    }

    @Test
    void logTest() {
        BuildVersion version = new BuildVersion();
        assertThat(version.logProperties())
    }


}