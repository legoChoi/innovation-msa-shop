package com.sparta.msa_exam.auth.application;

import com.sparta.msa_exam.auth.domain.dto.request.AuthSignInRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignInResponse;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignUpResponse;
import com.sparta.msa_exam.auth.domain.entity.User;
import com.sparta.msa_exam.auth.common.exception.CustomRuntimeException;
import com.sparta.msa_exam.auth.common.exception.ExceptionMessage;
import com.sparta.msa_exam.auth.infra.redis.AuthRedisRepository;
import com.sparta.msa_exam.auth.infra.jpa.AuthJpaRepository;
import com.sparta.msa_exam.auth.common.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthJpaRepository authJpaRepository;
    private final AuthRedisRepository authRedisRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthSignInResponse signIn(AuthSignInRequest authSignInRequest) {
        // 계정 조회
        User user = authJpaRepository.findByUsername(authSignInRequest.username())
                .orElseThrow(() -> new CustomRuntimeException(ExceptionMessage.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(authSignInRequest.password(), user.getPassword())) {
            throw new CustomRuntimeException(ExceptionMessage.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.generateAccessToken(user.getId().toString());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId().toString());

        // Redis에 Refresh Token 저장
        authRedisRepository.setRefreshToken(user.getId(), refreshToken);

        return new AuthSignInResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthSignUpResponse signUp(AuthSignUpRequest authSignUpRequest) {
        // 중복 계정 조회
        if (authJpaRepository.existsByUsername(authSignUpRequest.username())) {
            throw new CustomRuntimeException(ExceptionMessage.DUPLICATED_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(authSignUpRequest.password());

        User user = new User(authSignUpRequest.username(), encodedPassword);
        authJpaRepository.save(user);

        String accessToken = jwtProvider.generateAccessToken(user.getId().toString());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId().toString());

        // Redis에 Refresh Token 저장
        authRedisRepository.setRefreshToken(user.getId(), refreshToken);

        return new AuthSignUpResponse(accessToken, refreshToken);
    }
}
