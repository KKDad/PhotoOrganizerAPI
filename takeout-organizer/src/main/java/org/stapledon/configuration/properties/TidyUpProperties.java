package org.stapledon.configuration.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties("tidy-up")
public class TidyUpProperties {

    private boolean verbose;

    private boolean isCopy;

    private boolean dryRun;
}
