package org.stapledon.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    private final StapledonKeyLocator keyLocator;

    @Value("${security.app.jwtCookieName}")
    private String jwtCookieName;

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return new DefaultJwtBuilder()
                .setClaims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .header().keyId(jwtCookieName).and()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .audience().add("photo-server").and()
                .signWith(keyLocator.signingKey(jwtCookieName)).compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) {
            return null;
        }
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .keyLocator(keyLocator)
                    .build()
                    .parseSignedClaims(token).getPayload();

        } catch (MalformedJwtException e) {
            log.debug("MalformedJwtException: {}", e.getMessage());
            return null;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
