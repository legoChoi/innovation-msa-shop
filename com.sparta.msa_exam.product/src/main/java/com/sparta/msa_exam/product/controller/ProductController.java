package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.dto.request.ProductCreateRequest;
import com.sparta.msa_exam.product.dto.response.ProductCreateResponse;
import com.sparta.msa_exam.product.dto.response.ProductFindListResponse;
import com.sparta.msa_exam.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(
            @RequestBody @Valid ProductCreateRequest productCreateRequest
    ) {
        ProductCreateResponse response = productService.createProduct(productCreateRequest);

        return ResponseEntity.created(null)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ProductFindListResponse> findAllProducts() {
        ProductFindListResponse response = productService.findAllProducts();

        return ResponseEntity.ok()
                .body(response);
    }
}
