package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.product.dto.response.ProductIdListResponse;
import com.sparta.msa_exam.product.dto.response.SingleProductIdResponse;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalProductService {

    private final ProductRepository productRepository;

    public ProductIdListResponse validateProductIds(ProductIdListRequest request) {
        // validate
        request.productIds()
                .forEach(product -> existsProductById(product.productId()));

        // SingleProductIdResponse 매핑
        List<SingleProductIdResponse> productIds = request.productIds().stream()
                .map(product -> new SingleProductIdResponse(product.productId()))
                .toList();

        return new ProductIdListResponse(productIds);
    }

    private void existsProductById(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product NotFound Exception"); // TODO throw Product NotFound exception
        }
    }
}
