package com.sparta.msa_exam.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey accessSecretKey;

    public JwtProvider(
            @Value("${jwt.access}") String accessSecretKey
    ) {
        this.accessSecretKey = new SecretKeySpec(
                accessSecretKey.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS512.key().build().getAlgorithm()
        );
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = parseClaims(token, accessSecretKey);
            return claims.getExpiration().after(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            log.warn("Error validating access token");
            return false;
        }
    }

    private Claims parseClaims(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
