package com.sparta.msa_exam.product.dto.response;

public record ProductCreateResponse(
        Long id,
        String name,
        Integer price
) {
}
