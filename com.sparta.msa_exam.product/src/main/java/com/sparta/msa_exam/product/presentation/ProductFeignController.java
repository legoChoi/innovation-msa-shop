package com.sparta.msa_exam.product.presentation;

import com.sparta.msa_exam.product.domain.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.product.domain.dto.response.ProductIdListResponse;
import com.sparta.msa_exam.product.application.ProductInternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feign/products")
@RequiredArgsConstructor
public class ProductFeignController {

    private final ProductInternalService productInternalService;

    @GetMapping("/products/fail")
    public void fail() {
        productInternalService.fail();
    }

    @PostMapping
    public ResponseEntity<ProductIdListResponse> validateProductIds(
            @RequestBody @Valid ProductIdListRequest productIdListRequest
    ) {
        ProductIdListResponse response = productInternalService.validateProductIds(productIdListRequest);

        return ResponseEntity.ok()
                .body(response);
    }
}
