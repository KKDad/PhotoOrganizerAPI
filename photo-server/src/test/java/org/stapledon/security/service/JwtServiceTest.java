package org.stapledon.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.stapledon.security.filter.UserInfoDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {


    @Spy
    private StapledonKeyLocator stapledonKeyLocator;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
     ReflectionTestUtils.setField(jwtService, "jwtCookieName", "test");
    }


    @Test
    void generateValidToken() {
        String jwtToken = jwtService.generateToken("test");

        assertThat(jwtToken).isNotNull();

        // verify the signing key is called
        verify(stapledonKeyLocator).signingKey("test");

        // Validate the token
        assertThat(jwtService.validateToken(jwtToken, UserInfoDetails.builder().username("test").build())).isTrue();


        // Check that we can extract the username from jwtToken
        String username = jwtService.extractUsername(jwtToken);
        assertThat(username).isEqualTo("test");
    }
}