package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.request.ProductFindListRequest;
import com.sparta.msa_exam.product.dto.response.ProductFindListResponse;
import com.sparta.msa_exam.product.service.InternalProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class InternalProductController {

    private final InternalProductService internalProductService;

    @PostMapping
    public ResponseEntity<ProductFindListResponse> checkAndFindProducts(
            @RequestBody @Valid ProductFindListRequest productFindListRequest
    ) {
        ProductFindListResponse response = internalProductService.checkAndFindProducts(productFindListRequest);

        return ResponseEntity.ok()
                .body(response);
    }
}
