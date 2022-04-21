package org.stapledon.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.stapledon.PhotoOrganizerApplication;

import java.io.IOException;
import java.util.Properties;

@Component
@Slf4j
public class BuildVersion {

    private Properties buildProps;

    public BuildVersion() {
        var url = PhotoOrganizerApplication.class.getClassLoader().getResource("META-INF/build-info.properties");
        if (url == null) {
            buildProps = new Properties();
            return;
        }
        var props = new Properties();
        try (var stream = url.openStream()) {
            props.load(stream);
            buildProps = props;
        } catch (IOException e) {
            log.error("Unable to load build properties");
        }
    }

    public String getBuildProperty(String key) {
        if (buildProps == null) {
            return null;
        }
        return (String) buildProps.getOrDefault(key, null);
    }
}
