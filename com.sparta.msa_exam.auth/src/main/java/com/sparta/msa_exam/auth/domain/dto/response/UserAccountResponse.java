package com.sparta.msa_exam.auth.domain.dto.response;

public record UserAccountResponse(
        Long userId,
        String username,
        String password
) {
}
