package com.sparta.msa_exam.user.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "중복된 아이디입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
