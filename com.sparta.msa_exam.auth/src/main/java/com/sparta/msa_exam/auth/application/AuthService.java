package com.sparta.msa_exam.auth.application;

import com.sparta.msa_exam.auth.domain.dto.request.AuthReissueRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignInRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.domain.dto.response.AuthReissueResponse;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignInResponse;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignUpResponse;
import com.sparta.msa_exam.auth.domain.dto.response.UserAccountResponse;
import com.sparta.msa_exam.auth.domain.entity.User;
import com.sparta.msa_exam.auth.common.exception.CustomRuntimeException;
import com.sparta.msa_exam.auth.common.exception.ExceptionMessage;
import com.sparta.msa_exam.auth.infra.feign.UserFeignClient;
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

    private final UserFeignClient userFeignClient;
    private final AuthJpaRepository authJpaRepository;
    private final AuthRedisRepository authRedisRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthSignInResponse signIn(AuthSignInRequest request) {
        // 계정 조회
        UserAccountResponse userAccount = userFeignClient.findUserByIdOrdUsername(null, request.username());

        if (!passwordEncoder.matches(request.password(), userAccount.password())) {
            throw new CustomRuntimeException(ExceptionMessage.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.generateAccessToken(userAccount.userId());
        String refreshToken = jwtProvider.generateRefreshToken(userAccount.userId());

        // Redis에 Refresh Token 저장
        authRedisRepository.setRefreshToken(refreshToken, userAccount.userId());

        return new AuthSignInResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthSignUpResponse signUp(AuthSignUpRequest request) {
        // 중복 계정 조회
        if (authJpaRepository.existsByUsername(request.username())) {
            throw new CustomRuntimeException(ExceptionMessage.DUPLICATED_USERNAME);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(request.username(), encodedPassword);
        authJpaRepository.save(user);

        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        // Redis에 Refresh Token 저장
        authRedisRepository.setRefreshToken(refreshToken,user.getId());

        return new AuthSignUpResponse(accessToken, refreshToken);
    }

    public AuthReissueResponse regenerateAccessToken(AuthReissueRequest request) {
        // validate refresh token
        String refreshToken = request.refreshToken();

        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new CustomRuntimeException(ExceptionMessage.INVALID_TOKEN);
        }

        Long userId = authRedisRepository.getRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionMessage.INVALID_TOKEN));

        User user = authJpaRepository.findById(userId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionMessage.USER_NOT_FOUND));

        String accessToken = jwtProvider.generateAccessToken(user.getId());

        return new AuthReissueResponse(accessToken);
    }
}
