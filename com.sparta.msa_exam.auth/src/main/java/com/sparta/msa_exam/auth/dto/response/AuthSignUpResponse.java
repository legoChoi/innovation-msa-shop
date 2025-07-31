package com.sparta.msa_exam.auth.dto.response;

public record AuthSignUpResponse(
        String accessToken,
        String refreshToken
) {
}
