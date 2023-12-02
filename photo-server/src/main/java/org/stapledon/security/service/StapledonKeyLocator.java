package org.stapledon.security.service;

import io.jsonwebtoken.JweHeader;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.LocatorAdapter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyPair;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StapledonKeyLocator extends LocatorAdapter<Key>
{
    private static final Map<String, KeyPair> keys = new ConcurrentHashMap<>();
    @Override
    public Key locate(JwsHeader header) {
        return getOrCreateKeyPair(header.getKeyId()).getPublic();
    }

    @Override
    public Key locate(JweHeader header) {
        return getOrCreateKeyPair(header.getKeyId()).getPublic();
    }

    public Key signingKey(String keyId) {
        return getOrCreateKeyPair(keyId).getPrivate();
    }

    private static KeyPair getOrCreateKeyPair(String keyId) {
        if (Strings.isEmpty(keyId)) {
            throw new IllegalArgumentException("Key ID cannot be null or empty.");
        }
        keys.computeIfAbsent(keyId, k -> Jwts.SIG.RS512.keyPair().build());
        return keys.get(keyId);
    }
}