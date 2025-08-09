package com.sparta.msa_exam.auth.application;

import com.sparta.msa_exam.auth.common.util.JwtProvider;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.domain.dto.request.UserCreateRequest;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignUpResponse;
import com.sparta.msa_exam.auth.domain.dto.response.UserAccountResponse;
import com.sparta.msa_exam.auth.domain.dto.response.UserCreateResponse;
import com.sparta.msa_exam.auth.infra.feign.UserFeignClient;
import com.sparta.msa_exam.auth.infra.redis.AuthRedisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {


    @Mock
    UserFeignClient userFeignClient;

    @Mock
    AuthRedisRepository authRedisRepository;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService;

    @Nested
    @DisplayName("[POST /auth/sign-up]")
    class SignUp {

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            Long userId = 1L;
            String username = "username";
            String hashedPassword = "hashedPassword";
            String plainPassword = "plainPassword";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            AuthSignUpRequest authSignUpRequest = new AuthSignUpRequest(username, plainPassword);
            UserCreateRequest userCreateRequest = new UserCreateRequest(authSignUpRequest.username(), hashedPassword);

            UserAccountResponse userAccountResponse = new UserAccountResponse(userId, username, hashedPassword);
            UserCreateResponse userCreateResponse = new UserCreateResponse(userId);

            given(passwordEncoder.encode(authSignUpRequest.password()))
                    .willReturn(hashedPassword);
            given(userFeignClient.createUser(userCreateRequest))
                    .willReturn(userCreateResponse);

            given(jwtProvider.generateAccessToken(userCreateResponse.userId()))
                    .willReturn(accessToken);
            given(jwtProvider.generateRefreshToken(userCreateResponse.userId()))
                    .willReturn(refreshToken);

            // when
            AuthSignUpResponse authSignUpResponse = authService.signUp(authSignUpRequest);

            // then
            assertEquals(accessToken, authSignUpResponse.accessToken());
            assertEquals(refreshToken, authSignUpResponse.refreshToken());

            verify(passwordEncoder, times(1))
                    .encode(authSignUpRequest.password());
            verify(userFeignClient, times(1))
                    .createUser(userCreateRequest);
            verify(jwtProvider, times(1))
                    .generateAccessToken(userAccountResponse.userId());
            verify(jwtProvider, times(1))
                    .generateRefreshToken(userAccountResponse.userId());
            verify(authRedisRepository, times(1))
                    .setRefreshToken(refreshToken, userAccountResponse.userId());
        }
    }
}