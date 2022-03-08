package org.stapledon.configuration;


import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class Elasticsearch {

    @Getter
    @Setter
    @Value("${elastic.hostAndPort}")
    private String hostAndPort;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        var clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(getHostAndPort())
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}


