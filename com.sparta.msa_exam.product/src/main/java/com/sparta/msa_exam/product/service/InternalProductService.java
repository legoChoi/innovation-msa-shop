package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.request.ProductFindListRequest;
import com.sparta.msa_exam.product.dto.response.ProductFindListResponse;
import com.sparta.msa_exam.product.dto.response.ProductFindSingleResponse;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalProductService {

    private final ProductRepository productRepository;

    public ProductFindListResponse checkAndFindProducts(ProductFindListRequest request) {
        List<Product> productList = request.productIds().stream()
                .map(product -> productRepository.findById(product.productId())
                        .orElseThrow()) // TODO throw Todo NotFound exception
                .toList();

        List<ProductFindSingleResponse> products = productList.stream()
                .map(ProductFindSingleResponse::of)
                .toList();

        return new ProductFindListResponse(products);
    }
}
