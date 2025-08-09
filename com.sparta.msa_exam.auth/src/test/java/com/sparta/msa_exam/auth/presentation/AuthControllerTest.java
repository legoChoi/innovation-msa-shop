package com.sparta.msa_exam.auth.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa_exam.auth.application.AuthService;
import com.sparta.msa_exam.auth.domain.dto.request.AuthReissueRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignInRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.domain.dto.response.AuthReissueResponse;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignInResponse;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignUpResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class) // @Nested Class @Order 적용
class AuthControllerTest {

    @MockitoBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Nested
    @Order(1)
    @DisplayName("[POST /auth/sign-in]")
    class SignIn {

        private AuthSignInRequest authSignInRequest;
        private AuthSignInResponse authSignInResponse;

        @BeforeEach
        void init() {
            String username = "username";
            String password = "password";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            authSignInRequest = new AuthSignInRequest(username, password);
            authSignInResponse = new AuthSignInResponse(accessToken, refreshToken);
        }

        @Test
        @DisplayName("[성공]")
        void success() throws Exception {
            // given
            given(authService.signIn(authSignInRequest))
                    .willReturn(authSignInResponse);

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/auth/sign-in")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authSignInRequest))
            );

            // then
            String result = resultActions.andReturn().getResponse().getContentAsString();
            AuthSignInResponse response = objectMapper.readValue(result, AuthSignInResponse.class);

            resultActions
                    .andExpect(status().isOk());

            assertThat(response)
                    .usingRecursiveComparison()
                    .isEqualTo(authSignInResponse);

            verify(authService, times(1))
                    .signIn(authSignInRequest);
        }
    }

    @Nested
    @Order(2)
    @DisplayName("[POST /auth/sign-up]")
    class SignUp {

        private AuthSignUpRequest authSignUpRequest;
        private AuthSignUpResponse authSignUpResponse;

        @BeforeEach
        void init() {
            String username = "username";
            String password = "password";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            authSignUpRequest = new AuthSignUpRequest(username, password);
            authSignUpResponse = new AuthSignUpResponse(accessToken, refreshToken);
        }

        @Test
        @DisplayName("[성공]")
        void success() throws Exception {
            // given
            given(authService.signUp(authSignUpRequest))
                    .willReturn(authSignUpResponse);

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/auth/sign-up")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authSignUpRequest))
            );

            // then
            String result = resultActions.andReturn().getResponse().getContentAsString();
            AuthSignUpResponse response = objectMapper.readValue(result, AuthSignUpResponse.class);

            resultActions
                    .andExpect(status().isCreated());

            assertThat(response)
                    .usingRecursiveComparison()
                    .isEqualTo(authSignUpResponse);

            verify(authService, times(1))
                    .signUp(authSignUpRequest);
        }
    }

    @Nested
    @Order(3)
    @DisplayName("[POST /auth/reissue]")
    class Reissue {

        private AuthReissueRequest authReissueRequest;
        private AuthReissueResponse authReissueResponse;

        @BeforeEach
        void init() {
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            authReissueRequest = new AuthReissueRequest(accessToken);
            authReissueResponse = new AuthReissueResponse(refreshToken);
        }

        @Test
        @DisplayName("[성공]")
        void success() throws Exception {
            // given
            given(authService.regenerateAccessToken(authReissueRequest))
                    .willReturn(authReissueResponse);

            // when
            ResultActions resultActions = mockMvc.perform(
                    post("/auth/reissue")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authReissueRequest))
            );

            // then
            String result = resultActions.andReturn().getResponse().getContentAsString();
            AuthReissueResponse response = objectMapper.readValue(result, AuthReissueResponse.class);

            resultActions
                    .andExpect(status().isOk());

            assertThat(response)
                    .usingRecursiveComparison()
                    .isEqualTo(authReissueResponse);

            verify(authService, times(1))
                    .regenerateAccessToken(authReissueRequest);
        }
    }
}