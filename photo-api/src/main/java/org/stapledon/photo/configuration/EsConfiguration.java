package org.stapledon.photo.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Configuration class for Elasticsearch related settings.
 * Based on https://github.com/engagetech/dropwizard-elasticsearch/blob/master/src/main/java/io/dropwizard/elasticsearch/config/EsConfiguration.java
 */
public class EsConfiguration {

    @JsonProperty
    @NotNull
    private List<String> servers = new ArrayList<>();

    @JsonProperty
    private String clusterName = "elasticsearch";

    @JsonProperty
    @NotNull
    private Map<String, String> settings = new HashMap<>();

    @JsonProperty
    private String settingsFile = null;

    public List<String> getServers() {
        return servers;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public String getSettingsFile() {
        return settingsFile;
    }
}