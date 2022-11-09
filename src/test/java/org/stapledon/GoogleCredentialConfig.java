package org.stapledon;

import com.google.api.gax.core.CredentialsProvider;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestComponent
public class GoogleCredentialConfig {

    @Bean
    public CredentialsProvider googleCredentials() throws Exception {
        return mock(CredentialsProvider.class);
    }
}
