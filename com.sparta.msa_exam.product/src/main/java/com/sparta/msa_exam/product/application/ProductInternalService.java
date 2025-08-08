package com.sparta.msa_exam.product.application;

import com.sparta.msa_exam.product.domain.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.product.domain.dto.response.ProductIdListResponse;
import com.sparta.msa_exam.product.domain.dto.response.SingleProductIdResponse;
import com.sparta.msa_exam.product.common.exception.CustomRuntimeException;
import com.sparta.msa_exam.product.common.exception.ExceptionMessage;
import com.sparta.msa_exam.product.infra.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductInternalService {

    private final ProductJpaRepository productJpaRepository;

    public ProductIdListResponse validateProductIds(ProductIdListRequest request, boolean fail) {
        if (fail) {
            throw new CustomRuntimeException(ExceptionMessage.PRODUCT_SERVICE_UNAVAILABLE);
        }

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
        if (!productJpaRepository.existsById(productId)) {
            throw new CustomRuntimeException(ExceptionMessage.PRODUCT_NOT_FOUND);
        }
    }
}
