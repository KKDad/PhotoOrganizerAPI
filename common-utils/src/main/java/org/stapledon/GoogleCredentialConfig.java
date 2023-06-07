package org.stapledon;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class GoogleCredentialConfig {
//    @Bean
//    @ConditionalOnMissingBean(CredentialsProvider.class)
//    public CredentialsProvider googleCredentials() throws Exception {
//        return Mockito.mock(CredentialsProvider.class);
//    }

    @Bean
    @Qualifier("fooooooooo")
    public String fooBean() {
        return "fooooooooo";
    }
}
