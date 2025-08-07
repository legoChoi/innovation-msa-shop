package com.sparta.msa_exam.user.domain.dto.request;

public record UserCreateRequest(
        String username,
        String password
) {
}
