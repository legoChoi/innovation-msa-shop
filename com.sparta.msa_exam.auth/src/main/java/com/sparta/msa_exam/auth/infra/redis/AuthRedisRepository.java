package com.sparta.msa_exam.auth.infra.redis;

import java.util.Optional;

public interface AuthRedisRepository {

    void setRefreshToken(String refreshToken, Long userId);

    Optional<Long> getRefreshToken(String refreshToken);
}
