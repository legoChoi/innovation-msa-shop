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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class) // @Nested Class @Order 적용
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
    @DisplayName("[POST /auth/sign-in]")
    @Order(1)
    class SignIn {

        private AuthSignInRequest authSignInRequest;
        private UserAccountResponse userAccountResponse;

        @BeforeEach
        void init() {
            Long userId = 1L;
            String username = "username";
            String plainPassword = "plainPassword";
            String hashedPassword = "hashedPassword";

            authSignInRequest = new AuthSignInRequest(username, plainPassword);
            userAccountResponse = new UserAccountResponse(userId, username, hashedPassword);
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            String username = "username";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

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

        @Test
        @DisplayName("[실패] - 사용자가 입력한 비밀번호가 암호화된 비밀번호와 일치하지 않는 경우")
        void passwordMismatch() {
            // given
            String username = "username";

            given(userFeignClient.findUserByIdOrdUsername(null, username))
                    .willReturn(userAccountResponse);
            given(passwordEncoder.matches(authSignInRequest.password(), userAccountResponse.password()))
                    .willReturn(false);

            // when
            CustomRuntimeException exception = assertThrows(CustomRuntimeException.class,
                    () -> authService.signIn(authSignInRequest));

            // then
            assertEquals(ExceptionMessage.INVALID_CREDENTIALS.getStatus(), exception.getStatus());
            assertEquals(ExceptionMessage.INVALID_CREDENTIALS.getMessage(), exception.getMessage());

            verify(userFeignClient, times(1))
                    .findUserByIdOrdUsername(null, authSignInRequest.username());
            verify(passwordEncoder, times(1))
                    .matches(authSignInRequest.password(), userAccountResponse.password());
        }
    }

    @Nested
    @DisplayName("[POST /auth/sign-up]")
    @Order(2)
    class SignUp {

        private AuthSignUpRequest authSignUpRequest;
        private UserCreateRequest userCreateRequest;
        private UserAccountResponse userAccountResponse;
        private UserCreateResponse userCreateResponse;

        @BeforeEach
        void init() {
            Long userId = 1L;
            String username = "username";
            String plainPassword = "plain password";
            String hashedPassword = "hashedPassword";

            authSignUpRequest = new AuthSignUpRequest(username, plainPassword);
            userCreateRequest = new UserCreateRequest(username, hashedPassword);
            userAccountResponse = new UserAccountResponse(userId, username, hashedPassword);
            userCreateResponse = new UserCreateResponse(userId);
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            String hashedPassword = "hashedPassword";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

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

        private AuthReissueRequest authReissueRequest;
        private UserAccountResponse userAccountResponse;

        @BeforeEach
        void init() {
            Long userId = 1L;
            String username = "username";
            String hashedPassword = "hashedPassword";
            String oldRefreshToken = "old refreshToken";

            authReissueRequest = new AuthReissueRequest(oldRefreshToken);
            userAccountResponse = new UserAccountResponse(userId, username, hashedPassword);
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            Long userId = 1L;
            String newAccessToken = "new accessToken";

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
            given(jwtProvider.validateRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(false);

            // when
            CustomRuntimeException exception = assertThrows(CustomRuntimeException.class,
                    () -> authService.regenerateAccessToken(authReissueRequest));

            // then
            verify(jwtProvider, times(1))
                    .validateRefreshToken(authReissueRequest.refreshToken());

            assertEquals(ExceptionMessage.INVALID_TOKEN.getStatus(), exception.getStatus());
            assertEquals(ExceptionMessage.INVALID_TOKEN.getMessage(), exception.getMessage());
        }

        @Test
        @DisplayName("[실패] - redis 내 refresh token이 존재하지 않는 경우")
        void notExistsRefreshToken() {
            // given
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

            assertEquals(ExceptionMessage.INVALID_TOKEN.getStatus(), exception.getStatus());
            assertEquals(ExceptionMessage.INVALID_TOKEN.getMessage(), exception.getMessage());
        }
    }
}