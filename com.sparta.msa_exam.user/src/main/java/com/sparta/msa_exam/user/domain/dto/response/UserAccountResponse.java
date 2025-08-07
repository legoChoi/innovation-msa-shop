package com.sparta.msa_exam.user.domain.dto.response;

public record UserAccountResponse(
        Long userId,
        String username,
        String password
) {
}
