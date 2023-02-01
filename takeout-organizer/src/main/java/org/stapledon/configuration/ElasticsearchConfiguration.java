package org.stapledon.configuration;


import lombok.Getter;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Getter
@EnableElasticsearchRepositories(basePackages = "org.stapledon.es.repository")
public class ElasticsearchConfiguration {

    @Value("${elastic.hostAndPort}")
    private String hostAndPort;

    @Value("${elastic.password}")
    private String password;


    @Bean
    public RestHighLevelClient restHighLevelClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        var clientConfiguration = ClientConfiguration.builder()
                                                    .connectedTo(getHostAndPort())
                                                    .usingSsl(SSLContexts.custom().loadTrustMaterial(new TrustAllStrategy()).build(), new NoopHostnameVerifier())
                                                    .withBasicAuth("elastic", password)
                                                    .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new ElasticsearchRestTemplate(restHighLevelClient());
    }
}


