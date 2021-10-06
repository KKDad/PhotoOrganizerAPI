package org.stapledon.photo.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class PhotoAPIConfiguration extends Configuration {

    @JsonProperty("elastic")
    public ElasticsearchConfig elasticsearchConfig;

}
