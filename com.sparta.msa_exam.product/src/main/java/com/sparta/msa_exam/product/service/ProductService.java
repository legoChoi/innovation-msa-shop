package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.request.ProductCreateRequest;
import com.sparta.msa_exam.product.dto.response.ProductCreateResponse;
import com.sparta.msa_exam.product.dto.response.ProductFindDetailListResponse;
import com.sparta.msa_exam.product.dto.response.SingleProductDetailResponse;
import com.sparta.msa_exam.product.entity.Product;
import com.sparta.msa_exam.product.repository.ProductRedisRepository;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRedisRepository productRedisRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(request.name(), request.price());
        productRepository.save(product);

        return new ProductCreateResponse(product.getId(), product.getName(), product.getSupplyPrice());
    }

    public ProductFindDetailListResponse findAllProducts() {
        List<Product> productList = productRedisRepository.getProductAll();

        if (productList.isEmpty()) {
            productList = productRepository.findAll();
            productRedisRepository.setProductAll(productList);
        }

        List<SingleProductDetailResponse> products = productList.stream()
                .map(SingleProductDetailResponse::of)
                .toList();

        return new ProductFindDetailListResponse(products);
    }
}
