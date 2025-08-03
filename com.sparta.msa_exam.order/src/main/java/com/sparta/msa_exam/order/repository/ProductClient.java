package com.sparta.msa_exam.order.repository;

import com.sparta.msa_exam.order.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.order.dto.response.ProductDetailListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service", fallbackFactory = ProductClientFallbackFactory.class)
public interface ProductClient {

    /**
     * 서비스 통신 실패 가정 service unavailable
     */
    @GetMapping("/internal/products/fail")
    void fail();

    /**
     * Product Id 무결성 검증
     */
    @PostMapping("/internal/products")
    ProductDetailListResponse checkProductsExist(@RequestBody ProductIdListRequest request);
}
