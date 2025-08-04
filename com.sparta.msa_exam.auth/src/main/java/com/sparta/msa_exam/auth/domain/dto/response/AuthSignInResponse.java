package com.sparta.msa_exam.auth.domain.dto.response;

public record AuthSignInResponse(
        String accessToken,
        String refreshToken
) {
}
