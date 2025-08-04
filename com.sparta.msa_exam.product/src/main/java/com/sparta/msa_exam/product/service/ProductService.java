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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRedisRepository productRedisRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(request.name(), request.price());
        productRepository.save(product);

        productRedisRepository.addProductList(product);

        return new ProductCreateResponse(product.getId(), product.getName(), product.getSupplyPrice());
    }

    public ProductFindDetailListResponse findAllProducts() {
        // 캐싱된 상품 목록 확인
        List<Product> productList = productRedisRepository.getProductList();

        // 캐싱된 상품 목록이 존재하지 않으면 DB 조회 후 Redis 캐싱
        if (productList.isEmpty()) {
            productList = productRepository.findAll();
            productRedisRepository.setProductList(productList);
        }

        // SingleProductDetailResponse 매핑
        List<SingleProductDetailResponse> products = productList.stream()
                .map(SingleProductDetailResponse::of)
                .toList();

        return new ProductFindDetailListResponse(products);
    }
}
