package org.stapledon.configuration.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "organizer")
public class OrganizerProperties {

    private TidyUpProperties tidyUp;
    private DestinationProperties destination;
    private TakeoutProperties takeout;
}
