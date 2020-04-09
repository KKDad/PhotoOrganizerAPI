package org.stapledon.photo.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.elasticsearch.config.EsConfiguration;

public class PhotoAPIConfiguration extends Configuration {
    private EsConfiguration esConfiguration;

    @JsonProperty("EsConfiguration")
    public EsConfiguration getEsConfiguration() {
        return this.esConfiguration;
    }

    public void setEsConfiguration(EsConfiguration esConfiguration) {
        this.esConfiguration = esConfiguration;
    }
}
