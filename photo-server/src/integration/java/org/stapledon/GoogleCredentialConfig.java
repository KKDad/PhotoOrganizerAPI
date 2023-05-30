package org.stapledon;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.client5.http.auth.CredentialsProvider;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

public class GoogleCredentialConfig {
    @Bean
    public CredentialsProvider googleCredentials() throws Exception {
        return mock(CredentialsProvider.class);
    }
}
