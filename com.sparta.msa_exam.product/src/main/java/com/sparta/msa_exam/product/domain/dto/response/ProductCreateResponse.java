package com.sparta.msa_exam.product.domain.dto.response;

public record ProductCreateResponse(
        Long id,
        String name,
        Integer price
) {
}
