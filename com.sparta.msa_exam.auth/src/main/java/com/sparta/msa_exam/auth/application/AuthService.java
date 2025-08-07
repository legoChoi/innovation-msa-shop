package com.sparta.msa_exam.auth.application;

import com.sparta.msa_exam.auth.common.exception.CustomRuntimeException;
import com.sparta.msa_exam.auth.common.exception.ExceptionMessage;
import com.sparta.msa_exam.auth.common.util.JwtProvider;
import com.sparta.msa_exam.auth.domain.dto.request.AuthReissueRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignInRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.domain.dto.request.UserCreateRequest;
import com.sparta.msa_exam.auth.domain.dto.response.*;
import com.sparta.msa_exam.auth.infra.feign.UserFeignClient;
import com.sparta.msa_exam.auth.infra.redis.AuthRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserFeignClient userFeignClient;
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
        String encodedPassword = passwordEncoder.encode(request.password());

        UserCreateResponse user
                = userFeignClient.createUser(new UserCreateRequest(request.username(), encodedPassword));

        String accessToken = jwtProvider.generateAccessToken(user.userId());
        String refreshToken = jwtProvider.generateRefreshToken(user.userId());

        // Redis에 Refresh Token 저장
        authRedisRepository.setRefreshToken(refreshToken, user.userId());

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

        UserAccountResponse userAccount = userFeignClient.findUserByIdOrdUsername(userId, null);

        String accessToken = jwtProvider.generateAccessToken(userAccount.userId());

        return new AuthReissueResponse(accessToken);
    }
}
