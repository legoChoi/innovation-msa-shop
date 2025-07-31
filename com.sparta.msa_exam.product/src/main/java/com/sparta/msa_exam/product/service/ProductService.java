package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.request.ProductCreateRequest;
import com.sparta.msa_exam.product.dto.response.ProductCreateResponse;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(request.name(), request.price());
        productRepository.save(product);

        return new ProductCreateResponse(product.getId(), product.getName(), product.getSupplyPrice());
    }
}
