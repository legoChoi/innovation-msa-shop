package com.sparta.msa_exam.product.domain.dto.request;

public record ProductCreateRequest(
        String name,
        Integer price
) {
}
