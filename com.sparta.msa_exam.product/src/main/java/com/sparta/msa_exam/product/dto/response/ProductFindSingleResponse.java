package com.sparta.msa_exam.product.dto.response;

import com.sparta.msa_exam.product.entity.Product;

public record ProductFindSingleResponse(
        Long productId,
        String name,
        Integer price
) {

    public static ProductFindSingleResponse of(Product product) {
        return new ProductFindSingleResponse(
                product.getId(),
                product.getName(),
                product.getSupplyPrice()
        );
    }
}
