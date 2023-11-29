package org.stapledon.security.service;

import io.jsonwebtoken.JweHeader;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.LocatorAdapter;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

@Component
public class StapledonKeyLocator extends LocatorAdapter<Key>
{
    private static final Map<String, KeyPair> keys = new HashMap<>();
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
        keys.computeIfAbsent(keyId, k -> Jwts.SIG.RS512.keyPair().build());
        return keys.get(keyId);
    }
}