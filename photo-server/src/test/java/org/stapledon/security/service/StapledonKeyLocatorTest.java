package org.stapledon.security.service;

import io.jsonwebtoken.JweHeader;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.impl.DefaultJweHeader;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Key;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StapledonKeyLocatorTest {

    @InjectMocks
    private StapledonKeyLocator stapledonKeyLocator;

    @BeforeEach
    void setUp() {
        stapledonKeyLocator = new StapledonKeyLocator();
    }

    @Test
    void testLocateJwsHeaderReturnsValidKey() {
        // Arrange
        JwsHeader input = new DefaultJwsHeader(Map.of("kid", "test"));

        // Act
        Key result = stapledonKeyLocator.locate(input);
        Key signingKey = stapledonKeyLocator.signingKey("test");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAlgorithm()).isEqualTo("RSA");
        assertThat(result.getFormat()).isEqualTo("X.509");
        assertThat(result.getEncoded()).isNotEmpty();

        assertThat(signingKey).isNotNull();
        assertThat(signingKey.getAlgorithm()).isEqualTo("RSA");
        assertThat(signingKey.getFormat()).isEqualTo("PKCS#8");
        assertThat(signingKey.getEncoded()).isNotEmpty();
    }

    @Test
    void testLocateJweHeaderReturnsValidKey() {
        // Arrange
        JweHeader input = new DefaultJweHeader(Map.of("kid", "test"));

        // Act
        Key result = stapledonKeyLocator.locate(input);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAlgorithm()).isEqualTo("RSA");
        assertThat(result.getFormat()).isEqualTo("X.509");
        assertThat(result.getEncoded()).isNotEmpty();
    }

}