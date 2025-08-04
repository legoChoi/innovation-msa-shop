package com.sparta.msa_exam.auth.domain.dto.request;

public record AuthSignInRequest(
        String username,
        String password
) {
}
