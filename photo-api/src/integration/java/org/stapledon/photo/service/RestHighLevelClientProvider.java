package org.stapledon.photo.service;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stapledon.photo.configuration.ElasticsearchConfig;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class RestHighLevelClientProvider {

    private static final int DEFAULT_CONNECT_TIMEOUT = 2000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 30000;

    private static final Logger LOG = LoggerFactory.getLogger(RestHighLevelClientProvider.class);

    private static RestHighLevelClient restClient;


    // Utility Class
    private RestHighLevelClientProvider() {
    }


    public static synchronized RestHighLevelClient get(ElasticsearchConfig config) {
        if (restClient != null) {

            RestClientBuilder builder = RestClient.builder(extractESHosts(config.hosts)
                    .stream()
                    .map(http -> new HttpHost(http.hostname, http.port, Boolean.TRUE.equals(config.enableSSL) ? "https" : "http"))
                    .toArray(HttpHost[]::new))
                    .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                            .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT));

            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                try {
                    if (config.needsKeystore())
                        addKeystore(config, httpClientBuilder, config.keystorePath, config.keystorePassword);
                    if (config.needsTruststore())
                        addKeystore(config, httpClientBuilder, config.truststorePath, config.truststorePassword);
                    if (config.needsAuth())
                        addCredentials(config, httpClientBuilder);
                    if (Boolean.TRUE.equals(config.allowSelfSigned))
                        enableSelfSignedCertificates(httpClientBuilder);

                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
                return httpClientBuilder;
            });
            restClient = new RestHighLevelClient(builder);
        }
        return restClient;
    }

    private static List<Host> extractESHosts(String esHosts) {
        var defaultPort = 9200;
        return Arrays.stream(esHosts.split(","))
                .map(host -> host.split(":"))
                .map(hostParts -> {

                    int port = defaultPort;
                    if (hostParts.length > 1) {
                        try {
                            port = Integer.parseInt(hostParts[1].trim());
                        } catch (NumberFormatException e) {
                            LOG.warn("Unable to parse {} to valid port. Defaulting to {}.", hostParts[1], defaultPort, e);
                        }
                    }
                    return new Host(hostParts[0].trim(), port);
                })
                .collect(Collectors.toList());
    }

    private static void addKeystore(ElasticsearchConfig config, HttpAsyncClientBuilder httpClientBuilder, String path, String password) throws IOException {
        try {
            httpClientBuilder.setSSLContext(createSSLContext(path, password));
        } catch (IOException | GeneralSecurityException ex) {
            throw new IOException("Unable to load keystore '" + config.keystorePath + "': " + ex);
        }
    }

    private static void enableSelfSignedCertificates(HttpAsyncClientBuilder httpClientBuilder) throws IOException {
        try {
            httpClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier());
            httpClientBuilder.setSSLContext(SSLContexts.custom().loadTrustMaterial(new TrustAllStrategy()).build());
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException ex) {
            throw new IOException("Failed to enable self-signed certificates: " + ex);
        }
    }

    private static SSLContext createSSLContext(String keystore, String password) throws GeneralSecurityException, IOException {
        var sslContext = SSLContext.getInstance("TLS");
        KeyStore truststore;
        try (var inputStream = new FileInputStream(keystore)) {
            truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(inputStream, password.toCharArray());
        }

        var tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(truststore);

        X509TrustManager customTrustManager = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                customTrustManager = (X509TrustManager) tm;
                break;
            }
        }
        sslContext.init(null, new TrustManager[]{customTrustManager}, null);
        return sslContext;
    }

    private static void addCredentials(ElasticsearchConfig config, HttpAsyncClientBuilder httpAsyncClientBuilder) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(config.user, config.password));
        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    }

    private static class Host {
        String hostname;
        int port;

        Host(String hostname, int port) {
            this.hostname = hostname;
            this.port = port;
        }
    }
}