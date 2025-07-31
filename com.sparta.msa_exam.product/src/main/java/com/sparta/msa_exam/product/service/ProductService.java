package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.request.ProductCreateRequest;
import com.sparta.msa_exam.product.dto.response.ProductCreateResponse;
import com.sparta.msa_exam.product.dto.response.ProductFindListResponse;
import com.sparta.msa_exam.product.dto.response.ProductFindSingleResponse;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(request.name(), request.price());
        productRepository.save(product);

        return new ProductCreateResponse(product.getId(), product.getName(), product.getSupplyPrice());
    }

    public ProductFindListResponse findAllProducts() {
        List<Product> productList = productRepository.findAll();

        List<ProductFindSingleResponse> products = productList.stream()
                .map(ProductFindSingleResponse::of)
                .toList();

        return new ProductFindListResponse(products);
    }
}
