package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.request.ProductIdsRequest;
import com.sparta.msa_exam.product.dto.response.ProductIdsResponse;
import com.sparta.msa_exam.product.dto.response.SingleProductIdResponse;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalProductService {

    private final ProductRepository productRepository;

    public ProductIdsResponse checkAndFindProducts(ProductIdsRequest request) {
        request.productIds()
                .forEach(product -> isExistsProductById(product.productId()));

        List<SingleProductIdResponse> productIds = request.productIds().stream()
                .map(product -> new SingleProductIdResponse(product.productId()))
                .toList();

        return new ProductIdsResponse(productIds);
    }

    private void isExistsProductById(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product NotFound Exception"); // TODO throw Product NotFound exception
        }
    }
}
