package org.stapledon.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.stapledon.security.entities.UserInfo;
import org.stapledon.security.mapper.AccountMapper;
import org.stapledon.security.repository.UserInfoRepository;
import org.stapledon.security.service.JwtService;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    JwtService jwtService;

    @Mock
    AccountMapper mapper;

    @Mock
    UserInfoRepository repository;

    @InjectMocks
    JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        // Set up a SecurityContext
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testDoFilterInternal_validToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        UserInfoDetails userDetails = mock(UserInfoDetails.class);
        UserInfo userInfo = mock(UserInfo.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtService.extractUsername("validToken")).thenReturn("validUsername");
        when(repository.findByUsername("validUsername")).thenReturn(Optional.of(userInfo));
        when(mapper.toUserInfoDetails(userInfo)).thenReturn(userDetails);
        when(jwtService.validateToken("validToken", userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, times(1)).validateToken("validToken", userDetails);
        verify(jwtService, times(1)).extractUsername("validToken");
        verify(repository, times(1)).findByUsername("validUsername");
        verify(mapper, times(1)).toUserInfoDetails(userInfo);

        // verify the security context was set
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
    }

    @Test
    void testDoFilterInternal_userNotFound() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenReturn("invalidUsername");

        assertThrows(UsernameNotFoundException.class, () -> jwtAuthFilter.doFilterInternal(request, response, filterChain));

        // verify the security context was not set
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    void testDoFilterInternal_invalidToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.validateToken(eq("invalidToken"), any())).thenReturn(false);
        when(jwtService.extractUsername("invalidToken")).thenReturn("validUsername");
        when(repository.findByUsername("validUsername")).thenReturn(Optional.of(mock(UserInfo.class)));
        when(mapper.toUserInfoDetails(any())).thenReturn(mock(UserInfoDetails.class));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        verify(jwtService, times(1)).validateToken(eq("invalidToken"), any());
        verify(jwtService, times(1)).extractUsername("invalidToken");
        verify(repository, times(1)).findByUsername("validUsername");
        verify(mapper, times(1)).toUserInfoDetails(any());

        // verify the security context was not set
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }
}