package org.stapledon.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;
import org.stapledon.security.login.security.AccountDetailsImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    private static final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtCookie", "testCookie");
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 24 * 60 * 60 * 1000);
    }


    @Test
    void getJwtFromCookies_ReturnsJwt_WhenCookieExists() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("testCookie", "someJwt");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // Act
        String jwt = jwtUtils.getJwtFromCookies(request);

        // Assert
        assertEquals("someJwt", jwt);
    }

    @Test
    void getJwtFromCookies_ReturnsNull_WhenCookieDoesNotExist() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        // Act
        String jwt = jwtUtils.getJwtFromCookies(request);

        // Assert
        assertNull(jwt);
    }

    @Test
    void generateJwtCookie_ReturnsValidCookie_WhenUserPrincipalIsNotNull() {
        // Arrange
        AccountDetailsImpl userPrincipal = mock(AccountDetailsImpl.class);
        when(userPrincipal.getUsername()).thenReturn("testUser");

        // Act
        ResponseCookie cookie = jwtUtils.generateJwtCookie(userPrincipal);

        // Assert
        assertNotNull(cookie);
        assertEquals("testCookie", cookie.getName());
        assertEquals(jwtUtils.generateTokenFromUsername("testUser"), cookie.getValue());
        assertEquals("/api", cookie.getPath());
        assertEquals(Duration.ofSeconds(24 * 60 * 60L), cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    void getCleanJwtCookie_ReturnsEmptyCookie() {
        // Act
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        // Assert
        assertNotNull(cookie);
        assertEquals("testCookie", cookie.getName());
        assertEquals("", cookie.getValue());
        assertEquals("/api", cookie.getPath());
    }

    @Test
    void getUserNameFromJwtToken_ReturnsUserName_WhenTokenIsValid() {
        // Arrange
        String userName = "testUser";
        String token = jwtUtils.generateTokenFromUsername(userName);

        // Act
        String result = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertNotNull(result);
        assertEquals(userName, result);
    }

    @Test
    void getUserNameFromJwtToken_ThrowsException_WhenTokenIsInvalid() {
        // Arrange
        String invalidToken = "invalid.token.string";

        // Assert
        assertThrows(JwtException.class, () -> {
            // Act
            jwtUtils.getUserNameFromJwtToken(invalidToken);
        });
    }

    @Test
    void generateTokenFromUsername_ReturnsValidToken_WhenUsernameIsNotNull() {
        // Arrange
        String username = "testUser";

        // Act
        String token = jwtUtils.generateTokenFromUsername(username);

        // Assert
        assertNotNull(token);
        Jws<Claims> claims = Jwts.parser().setSigningKey("testSecret").parseClaimsJws(token);
        assertEquals(username, claims.getBody().getSubject());
        assertTrue(claims.getBody().getExpiration().after(new Date()));
    }
}