package org.stapledon.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.stapledon.security.dto.AuthRequest;
import org.stapledon.security.service.AuthenticationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationService authenticationService;



    @Test
    void testAuthenticateAndGetToken() {
        AuthRequest authRequest = AuthRequest.builder()
                .username("Username").password("Password").build();
        when(authenticationService.authenticate(authRequest)).thenReturn("Token");

        String result = authController.authenticateAndGetToken(authRequest);
        assertThat(result).isEqualTo("Token");
    }

    @Test
    void AuthenticateNotAuthenticated() {
        AuthRequest authRequest = AuthRequest.builder()
                .username("Username").password("Password").build();

        when(authenticationService.authenticate(authRequest)).thenThrow(new UsernameNotFoundException("Username not found"));

        assertThatThrownBy(() -> authController.authenticateAndGetToken(authRequest))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Username not found");
    }
}