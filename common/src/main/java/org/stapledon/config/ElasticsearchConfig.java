package org.stapledon.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElasticsearchConfig {
    @JsonProperty("esHosts")
    public String esHosts;

    @JsonProperty("esUser")
    public String esUser;

    @JsonProperty("esPassword")
    public String esPassword;

    @JsonProperty("esEnableSSL")
    public Boolean esEnableSSL;

    @JsonProperty("esAllowSelfSigned")
    public Boolean esAllowSelfSigned;

    @JsonProperty("esKeystorePath")
    public String esKeystorePath;

    @JsonProperty("esKeystorePassword")
    public String esKeystorePassword;

    @JsonProperty("esTruststorePath")
    public String esTruststorePath;

    @JsonProperty("esTruststorePassword")
    public String esTruststorePassword;

    public boolean needsAuth() {
        return this.esUser != null && !this.esUser.isEmpty() && this.esPassword != null && !this.esPassword.isEmpty();
    }

    public boolean needsKeystore() {
        return this.esKeystorePath != null && !this.esKeystorePath.isEmpty() && this.esKeystorePassword != null
                && !this.esKeystorePassword.isEmpty();
    }

    public boolean needsTruststore() {
        return this.esTruststorePath != null && !this.esTruststorePath.isEmpty() && this.esTruststorePassword != null
                && !this.esTruststorePassword.isEmpty();
    }
}