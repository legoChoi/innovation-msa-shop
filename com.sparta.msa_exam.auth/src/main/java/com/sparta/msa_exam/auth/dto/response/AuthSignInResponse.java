package com.sparta.msa_exam.auth.dto.response;

public record AuthSignInResponse(
        String accessToken,
        String refreshToken
) {
}
