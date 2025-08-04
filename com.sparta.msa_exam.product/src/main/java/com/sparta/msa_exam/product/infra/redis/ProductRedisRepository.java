package com.sparta.msa_exam.product.infra.redis;

import com.sparta.msa_exam.product.domain.entity.Product;

import java.util.List;

public interface ProductRedisRepository {

    /**
     * Redis 내 캐싱 된 상품 목록 조회
     */
    List<Product> getProductList();

    /**
     * Redis 내 상품 목록 캐싱
     */
    void setProductList(List<Product> productList);

    /**
     * Redis 내 상품 목록에 새로운 상품 캐싱
     */
    void addProductList(Product product);
}
