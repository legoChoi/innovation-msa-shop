package com.sparta.msa_exam.product.dto.request;

public record ProductCreateRequest(
        String name,
        Integer price
) {
}
