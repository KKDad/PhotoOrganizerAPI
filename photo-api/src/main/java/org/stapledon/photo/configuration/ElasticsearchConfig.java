package org.stapledon.photo.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ElasticsearchConfig {
    @JsonProperty("hosts")
    public String hosts;

    @JsonProperty("user")
    public String user;

    @JsonProperty("password")
    public String password;

    @JsonProperty("enable_ssl")
    public Boolean enableSSL;

    @JsonProperty("allow_self_signed")
    public Boolean allowSelfSigned;

    @JsonProperty("keystorePath")
    public String keystorePath;

    @JsonProperty("keystorePassword")
    public String keystorePassword;

    @JsonProperty("truststorePath")
    public String truststorePath;

    @JsonProperty("truststorePassword")
    public String truststorePassword;

    @JsonProperty("num_shards")
    public String numShards;

    @JsonProperty("num_replicas")
    public String numReplicas;


    public boolean needsAuth() {
        return this.user != null && !this.user.isEmpty() && this.password != null && !this.password.isEmpty();
    }

    public boolean needsKeystore() {
        return this.keystorePath != null && !this.keystorePath.isEmpty() && this.keystorePassword != null
                && !this.keystorePassword.isEmpty();
    }

    public boolean needsTruststore() {
        return this.truststorePath != null && !this.truststorePath.isEmpty() && this.truststorePassword != null
                && !this.truststorePassword.isEmpty();
    }

    public List<String> getServers() {
        return List.of(hosts.split(","));
    }
}