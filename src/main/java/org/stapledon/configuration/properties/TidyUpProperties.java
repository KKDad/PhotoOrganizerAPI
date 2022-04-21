package org.stapledon.configuration.properties;

import lombok.Data;

@Data
public class TidyUpProperties {

    private boolean verbose;

    private boolean isCopy;

    private boolean dryRun;
}
