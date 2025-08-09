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

    private final Long USER_ID = 1L;
    private final String USERNAME = "username";
    private final String PLAIN_PASSWORD = "plain_password";
    private final String HASHED_PASSWORD = "hashed_password";
    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";
    private final String NEW_ACCESS_TOKEN = "new_access_token";

    @Nested
    @DisplayName("[POST /auth/sign-in]")
    @Order(1)
    class SignIn {

        private AuthSignInRequest authSignInRequest;
        private UserAccountResponse userAccountResponse;

        @BeforeEach
        void init() {
            authSignInRequest = new AuthSignInRequest(USERNAME, PLAIN_PASSWORD);
            userAccountResponse = new UserAccountResponse(USER_ID, USERNAME, HASHED_PASSWORD);
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            given(userFeignClient.findUserByIdOrdUsername(null, USERNAME))
                    .willReturn(userAccountResponse);
            given(passwordEncoder.matches(authSignInRequest.password(), userAccountResponse.password()))
                    .willReturn(true);
            given(jwtProvider.generateAccessToken(userAccountResponse.userId()))
                    .willReturn(ACCESS_TOKEN);
            given(jwtProvider.generateRefreshToken(userAccountResponse.userId()))
                    .willReturn(REFRESH_TOKEN);

            // when
            AuthSignInResponse authSignInResponse = authService.signIn(authSignInRequest);

            // then
            assertEquals(ACCESS_TOKEN, authSignInResponse.accessToken());
            assertEquals(REFRESH_TOKEN, authSignInResponse.refreshToken());

            verify(userFeignClient, times(1))
                    .findUserByIdOrdUsername(null, authSignInRequest.username());
            verify(passwordEncoder, times(1))
                    .matches(authSignInRequest.password(), userAccountResponse.password());
            verify(jwtProvider, times(1))
                    .generateAccessToken(userAccountResponse.userId());
            verify(jwtProvider, times(1))
                    .generateRefreshToken(userAccountResponse.userId());
            verify(authRedisRepository, times(1))
                    .setRefreshToken(REFRESH_TOKEN, userAccountResponse.userId());
        }

        @Test
        @DisplayName("[실패] - 사용자가 입력한 비밀번호가 암호화된 비밀번호와 일치하지 않는 경우")
        void passwordMismatch() {
            // given
            given(userFeignClient.findUserByIdOrdUsername(null, USERNAME))
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
            authSignUpRequest = new AuthSignUpRequest(USERNAME, PLAIN_PASSWORD);
            userCreateRequest = new UserCreateRequest(USERNAME, HASHED_PASSWORD);
            userAccountResponse = new UserAccountResponse(USER_ID, USERNAME, HASHED_PASSWORD);
            userCreateResponse = new UserCreateResponse(USER_ID);
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given
            given(passwordEncoder.encode(authSignUpRequest.password()))
                    .willReturn(HASHED_PASSWORD);
            given(userFeignClient.createUser(userCreateRequest))
                    .willReturn(userCreateResponse);

            given(jwtProvider.generateAccessToken(userCreateResponse.userId()))
                    .willReturn(ACCESS_TOKEN);
            given(jwtProvider.generateRefreshToken(userCreateResponse.userId()))
                    .willReturn(REFRESH_TOKEN);

            // when
            AuthSignUpResponse authSignUpResponse = authService.signUp(authSignUpRequest);

            // then
            assertEquals(ACCESS_TOKEN, authSignUpResponse.accessToken());
            assertEquals(REFRESH_TOKEN, authSignUpResponse.refreshToken());

            verify(passwordEncoder, times(1))
                    .encode(authSignUpRequest.password());
            verify(userFeignClient, times(1))
                    .createUser(userCreateRequest);
            verify(jwtProvider, times(1))
                    .generateAccessToken(userAccountResponse.userId());
            verify(jwtProvider, times(1))
                    .generateRefreshToken(userAccountResponse.userId());
            verify(authRedisRepository, times(1))
                    .setRefreshToken(REFRESH_TOKEN, userAccountResponse.userId());
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
            authReissueRequest = new AuthReissueRequest(REFRESH_TOKEN);
            userAccountResponse = new UserAccountResponse(USER_ID, USERNAME, HASHED_PASSWORD);
        }

        @Test
        @DisplayName("[성공]")
        void success() {
            // given

            given(jwtProvider.validateRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(true);
            given(authRedisRepository.getRefreshToken(authReissueRequest.refreshToken()))
                    .willReturn(Optional.of(USER_ID));
            given(userFeignClient.findUserByIdOrdUsername(USER_ID, null))
                    .willReturn(userAccountResponse);
            given(jwtProvider.generateAccessToken(userAccountResponse.userId()))
                    .willReturn(NEW_ACCESS_TOKEN);

            // when
            AuthReissueResponse authReissueResponse = authService.regenerateAccessToken(authReissueRequest);

            // then
            assertEquals(NEW_ACCESS_TOKEN, authReissueResponse.accessToken());

            verify(jwtProvider, times(1))
                    .validateRefreshToken(authReissueRequest.refreshToken());
            verify(authRedisRepository, times(1))
                    .getRefreshToken(authReissueRequest.refreshToken());
            verify(userFeignClient, times(1))
                    .findUserByIdOrdUsername(USER_ID, null);
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