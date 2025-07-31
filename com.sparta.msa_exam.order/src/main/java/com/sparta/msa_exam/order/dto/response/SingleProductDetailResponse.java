package com.sparta.msa_exam.order.dto.response;

public record SingleProductDetailResponse(
        Long productId,
        String name,
        Integer price
) {
}
