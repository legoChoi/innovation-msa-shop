package com.sparta.msa_exam.auth.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepositoryImpl implements AuthRedisRepository {

    private final String REDIS_PREFIX = "refresh::";

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setRefreshToken(String refreshToken, Long userId) {
        redisTemplate.opsForValue()
                .set(REDIS_PREFIX + refreshToken, userId.toString(), Duration.ofMillis(refreshExpiration));
    }

    @Override
    public Optional<Long> getRefreshToken(String refreshToken) {
        return Optional.of(
                Long.valueOf(redisTemplate.opsForValue().get(REDIS_PREFIX + refreshToken))
        );
    }
}
