package com.sparta.msa_exam.order.infra.feign;

import com.sparta.msa_exam.order.domain.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.order.domain.dto.response.ProductDetailListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service", fallbackFactory = ProductFeignClientFallbackFactory.class)
public interface ProductFeignClient {

    /**
     * Product Id 무결성 검증
     * fail == true 서비스 통신 실패 가정 service unavailable
     */
    @PostMapping("/feign/products/validation")
    ProductDetailListResponse validateProductIds(
            @RequestBody ProductIdListRequest request,
            @RequestParam boolean fail
    );
}
