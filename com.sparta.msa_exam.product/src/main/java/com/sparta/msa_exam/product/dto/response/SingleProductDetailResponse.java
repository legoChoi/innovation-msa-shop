package com.sparta.msa_exam.product.dto.response;

import com.sparta.msa_exam.product.entity.Product;

public record SingleProductDetailResponse(
        Long productId,
        String name,
        Integer price
) {

    public static SingleProductDetailResponse of(Product product) {
        return new SingleProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getSupplyPrice()
        );
    }
}
