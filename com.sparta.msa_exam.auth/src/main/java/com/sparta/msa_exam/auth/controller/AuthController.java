package com.sparta.msa_exam.auth.controller;

import com.sparta.msa_exam.auth.dto.request.AuthSignInRequest;
import com.sparta.msa_exam.auth.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.dto.response.AuthSignInResponse;
import com.sparta.msa_exam.auth.dto.response.AuthSignUpResponse;
import com.sparta.msa_exam.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthSignInResponse> signIn(
            @RequestBody @Valid AuthSignInRequest authSignInRequest
    ) {
        AuthSignInResponse response = authService.signIn(authSignInRequest);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthSignUpResponse> signUp(
            @RequestBody @Valid AuthSignUpRequest authSignUpRequest
    ) {
        AuthSignUpResponse response = authService.signUp(authSignUpRequest);

        return ResponseEntity.created(null)
                .body(response);
    }
}
