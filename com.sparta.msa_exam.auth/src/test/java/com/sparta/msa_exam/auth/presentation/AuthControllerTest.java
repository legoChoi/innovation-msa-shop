package com.sparta.msa_exam.auth.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa_exam.auth.application.AuthService;
import com.sparta.msa_exam.auth.domain.dto.request.AuthReissueRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignInRequest;
import com.sparta.msa_exam.auth.domain.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.domain.dto.response.AuthReissueResponse;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignInResponse;
import com.sparta.msa_exam.auth.domain.dto.response.AuthSignUpResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.*;
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

        @Test
        @DisplayName("[성공]")
        void success() throws Exception {
            // given
            String username = "username";
            String password = "password";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            AuthSignInRequest authSignInRequest = new AuthSignInRequest(username, password);
            AuthSignInResponse authSignInResponse = new AuthSignInResponse(accessToken, refreshToken);

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

        @Test
        @DisplayName("[성공]")
        void success() throws Exception {
            // given
            String username = "username";
            String password = "password";
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            AuthSignUpRequest authSignUpRequest = new AuthSignUpRequest(username, password);
            AuthSignUpResponse authSignUpResponse = new AuthSignUpResponse(accessToken, refreshToken);

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

        @Test
        @DisplayName("[성공]")
        void success() throws Exception {
            // given
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";

            AuthReissueRequest authReissueRequest = new AuthReissueRequest(refreshToken);
            AuthReissueResponse authReissueResponse = new AuthReissueResponse(accessToken);

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