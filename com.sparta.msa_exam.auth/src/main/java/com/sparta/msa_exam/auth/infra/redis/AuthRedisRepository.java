package com.sparta.msa_exam.auth.infra.redis;

public interface AuthRedisRepository {

    void setRefreshToken(Long userId, String refreshToken);
}
