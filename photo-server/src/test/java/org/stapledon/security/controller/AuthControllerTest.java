package org.stapledon.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.model.AuthRequest;
import org.stapledon.security.service.JwtService;
import org.stapledon.security.service.UserInfoService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Test
    void testAddNewUser() {
        // Arrange
        UserInfo userInfo = UserInfo.builder()
                .userId(5)
                .username("username")
                .password("password")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@stapledon.ca").build();
        when(userInfoService.addUser(any(UserInfo.class))).thenReturn(userInfo);

        // Act
        ResponseEntity<Object> result = authController.addNewUser(userInfo);


        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getHeaders().getLocation()).hasToString("/api/v1/users/5");

    }

    @Test
    void testAuthenticateAndGetToken() {
        // Arrange
        AuthRequest authRequest = AuthRequest.builder()
                .username("Username").password("Password").build();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("Token");

        // Act
        String result = authController.authenticateAndGetToken(authRequest);

        // Assert
        assertThat(result).isEqualTo("Token");
    }

    @Test
    void AuthenticateNotAuthenticated() {

        // Arrange
        AuthRequest authRequest = AuthRequest.builder()
                .username("Username").password("Password").build();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        assertThrows(UsernameNotFoundException.class, () -> authController.authenticateAndGetToken(authRequest));
    }
}