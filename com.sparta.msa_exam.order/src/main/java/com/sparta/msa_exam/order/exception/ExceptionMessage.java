package com.sparta.msa_exam.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "잠시 후에 주문 추가를 요청 해주세요.");

    private final HttpStatus status;
    private final String message;
}
