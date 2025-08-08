package com.sparta.msa_exam.product.application;

import com.sparta.msa_exam.product.domain.dto.request.ProductCreateRequest;
import com.sparta.msa_exam.product.domain.dto.response.ProductCreateResponse;
import com.sparta.msa_exam.product.domain.dto.response.ProductFindDetailListResponse;
import com.sparta.msa_exam.product.domain.dto.response.SingleProductDetailResponse;
import com.sparta.msa_exam.product.domain.entity.Product;
import com.sparta.msa_exam.product.infra.redis.ProductRedisRepository;
import com.sparta.msa_exam.product.infra.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductJpaRepository productJpaRepository;
    private final ProductRedisRepository productRedisRepository;

    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(request.name(), request.price());
        productJpaRepository.save(product);

        // 캐싱된 상품 목록 확인
        List<Product> productList = productRedisRepository.getProductList();

        // 캐싱된 상품 목록이 존재하면 상품 목록에 추가된 상품 추가 캐싱
        if (!productList.isEmpty()) {
            productRedisRepository.addProductList(product);
        }

        return new ProductCreateResponse(product.getId(), product.getName(), product.getSupplyPrice());
    }

    public ProductFindDetailListResponse findAllProducts() {
        // 캐싱된 상품 목록 확인
        List<Product> productList = productRedisRepository.getProductList();

        // 캐싱된 상품 목록이 존재하지 않으면 DB 조회 후 Redis 캐싱
        if (productList.isEmpty()) {
            productList = productJpaRepository.findAll();
            productRedisRepository.setProductList(productList);
        }

        // SingleProductDetailResponse 매핑
        List<SingleProductDetailResponse> products = productList.stream()
                .map(SingleProductDetailResponse::of)
                .toList();

        return new ProductFindDetailListResponse(products);
    }
}
