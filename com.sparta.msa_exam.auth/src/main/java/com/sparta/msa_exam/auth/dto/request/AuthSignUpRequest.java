package com.sparta.msa_exam.auth.dto.request;

public record AuthSignUpRequest(
        String username,
        String password
) {
}
