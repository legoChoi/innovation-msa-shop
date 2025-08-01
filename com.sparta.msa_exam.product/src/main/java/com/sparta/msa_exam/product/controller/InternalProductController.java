package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.product.dto.response.ProductIdListResponse;
import com.sparta.msa_exam.product.service.InternalProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class InternalProductController {

    private final InternalProductService internalProductService;

    @GetMapping("/internal/products/fail")
    public void fail() {
        throw new RuntimeException("service fail");
    }

    @PostMapping
    public ResponseEntity<ProductIdListResponse> checkAndFindProducts(
            @RequestBody @Valid ProductIdListRequest productIdListRequest
    ) {
        ProductIdListResponse response = internalProductService.checkAndFindProducts(productIdListRequest);

        return ResponseEntity.ok()
                .body(response);
    }
}
