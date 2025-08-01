package com.sparta.msa_exam.auth.repository;

public interface AuthRedisRepository {

    void setRefreshToken(Long userId, String refreshToken);
}
