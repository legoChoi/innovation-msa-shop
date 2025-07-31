package com.sparta.msa_exam.order.dto.response;

public record ProductDetailResponse(
        Long productId,
        String name,
        Integer price
) {
}
