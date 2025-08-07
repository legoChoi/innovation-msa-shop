package com.sparta.msa_exam.auth.domain.dto.request;

public record UserCreateRequest(
        String username,
        String password
) {
}
