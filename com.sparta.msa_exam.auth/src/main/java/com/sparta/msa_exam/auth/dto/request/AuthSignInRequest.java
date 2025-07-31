package com.sparta.msa_exam.auth.dto.request;

public record AuthSignInRequest(
        String username,
        String password
) {
}
