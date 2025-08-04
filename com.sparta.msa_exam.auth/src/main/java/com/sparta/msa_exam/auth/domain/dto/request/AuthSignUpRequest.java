package com.sparta.msa_exam.auth.domain.dto.request;

public record AuthSignUpRequest(
        String username,
        String password
) {
}
