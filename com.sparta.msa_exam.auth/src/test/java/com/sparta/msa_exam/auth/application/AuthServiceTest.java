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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class) // @Nested Class @Order 적용
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceTest.class);
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
    @DisplayName("[POST /auth/sign-in]")
    @Order(1)
    class SignIn {

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            Long userId = 1L;
            String username = "username";
            String plainPassword = "plainPassword";
            String hashedPassword = "hashedPassword";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            AuthSignInRequest authSignInRequest = new AuthSignInRequest(username, plainPassword);
            UserAccountResponse userAccountResponse
                    = new UserAccountResponse(userId, authSignInRequest.username(), hashedPassword);

            given(userFeignClient.findUserByIdOrdUsername(null, username))
                    .willReturn(userAccountResponse);
            given(passwordEncoder.matches(authSignInRequest.password(), userAccountResponse.password()))
                    .willReturn(true);
            given(jwtProvider.generateAccessToken(userAccountResponse.userId()))
                    .willReturn(accessToken);
            given(jwtProvider.generateRefreshToken(userAccountResponse.userId()))
                    .willReturn(refreshToken);

            // when
            AuthSignInResponse authSignInResponse = authService.signIn(authSignInRequest);

            // then
            assertEquals(accessToken, authSignInResponse.accessToken());
            assertEquals(refreshToken, authSignInResponse.refreshToken());

            verify(userFeignClient, times(1))
                    .findUserByIdOrdUsername(null, authSignInRequest.username());
            verify(passwordEncoder, times(1))
                    .matches(authSignInRequest.password(), userAccountResponse.password());
            verify(jwtProvider, times(1))
                    .generateAccessToken(userAccountResponse.userId());
            verify(jwtProvider, times(1))
                    .generateRefreshToken(userAccountResponse.userId());
            verify(authRedisRepository, times(1))
                    .setRefreshToken(refreshToken, userAccountResponse.userId());
        }
    }

    @Nested
    @DisplayName("[POST /auth/sign-up]")
    @Order(2)
    class SignUp {

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            Long userId = 1L;
            String username = "username";
            String plainPassword = "plainPassword";
            String hashedPassword = "hashedPassword";
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

    @Nested
    @DisplayName("[POST /auth/reissue]")
    @Order(3)
    class Reissue {

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            Long userId = 1L;
            String username = "username";
            String hashedPassword = "hashedPassword";
            String newAccessToken = "new accessToken";
            String refreshToken = "refreshToken";

            AuthReissueRequest authReissueRequest = new AuthReissueRequest(refreshToken);
            UserAccountResponse userAccountResponse = new UserAccountResponse(userId, username, hashedPassword);

            given(jwtProvider.validateRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(true);
            given(authRedisRepository.getRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(Optional.of(userId));
            given(userFeignClient.findUserByIdOrdUsername(userId, null))
                    .willReturn(userAccountResponse);
            given(jwtProvider.generateAccessToken(userAccountResponse.userId()))
                    .willReturn(newAccessToken);

            // when
            AuthReissueResponse authReissueResponse = authService.regenerateAccessToken(authReissueRequest);

            // then
            assertEquals(newAccessToken, authReissueResponse.accessToken());

            verify(jwtProvider, times(1))
                    .validateRefreshToken(authReissueRequest.refreshToken());
            verify(authRedisRepository, times(1))
                    .getRefreshToken(authReissueRequest.refreshToken());
            verify(userFeignClient, times(1))
                    .findUserByIdOrdUsername(userId, null);
            verify(jwtProvider, times(1))
                    .generateAccessToken(userAccountResponse.userId());
        }

        @Test
        @DisplayName("[실패] - 유효하지 않은 refresh token를 전달한 경우")
        void invalidRefreshToken() {
            // given
            String refreshToken = "refreshToken";
            ExceptionMessage invalidTokenException = ExceptionMessage.INVALID_TOKEN;

            AuthReissueRequest authReissueRequest = new AuthReissueRequest(refreshToken);

            given(jwtProvider.validateRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(false);

            // when
            CustomRuntimeException exception = assertThrows(CustomRuntimeException.class,
                    () -> authService.regenerateAccessToken(authReissueRequest));

            // then
            verify(jwtProvider, times(1))
                    .validateRefreshToken(authReissueRequest.refreshToken());

            assertEquals(invalidTokenException.getStatus(), exception.getStatus());
            assertEquals(invalidTokenException.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("[실패] - redis 내 refresh token이 존재하지 않는 경우")
        void notExistsRefreshToken() {
            // given
            String refreshToken = "refreshToken";
            ExceptionMessage invalidTokenException = ExceptionMessage.INVALID_TOKEN;

            AuthReissueRequest authReissueRequest = new AuthReissueRequest(refreshToken);

            given(jwtProvider.validateRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(true);
            given(authRedisRepository.getRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(Optional.empty());

            // when
            CustomRuntimeException exception = assertThrows(CustomRuntimeException.class,
                    () -> authService.regenerateAccessToken(authReissueRequest));

            // then
            verify(jwtProvider, times(1))
                    .validateRefreshToken(authReissueRequest.refreshToken());
            verify(authRedisRepository, times(1))
                    .getRefreshToken(authReissueRequest.refreshToken());

            assertEquals(invalidTokenException.getStatus(), exception.getStatus());
            assertEquals(invalidTokenException.getMessage(), exception.getMessage());
        }
    }
}