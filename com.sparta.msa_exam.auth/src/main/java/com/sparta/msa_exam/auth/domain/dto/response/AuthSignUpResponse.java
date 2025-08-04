package com.sparta.msa_exam.auth.domain.dto.response;

public record AuthSignUpResponse(
        String accessToken,
        String refreshToken
) {
}
