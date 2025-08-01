package com.sparta.msa_exam.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class AuthRedisRepositoryImpl implements AuthRedisRepository{

    private final String REDIS_PREFIX = "refresh::";

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setRefreshToken(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(REDIS_PREFIX + userId, refreshToken, Duration.ofMillis(refreshExpiration));
    }
}
