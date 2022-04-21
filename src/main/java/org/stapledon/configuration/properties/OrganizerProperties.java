package org.stapledon.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "organizer")
public class OrganizerProperties {

    private TidyUpProperties tidyUp;
    private DestinationProperties destination;
    private TakeoutProperties takeout;
}
