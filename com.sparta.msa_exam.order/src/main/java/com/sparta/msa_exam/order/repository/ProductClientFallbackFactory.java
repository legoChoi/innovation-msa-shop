package com.sparta.msa_exam.order.repository;

import com.sparta.msa_exam.order.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.order.dto.response.ProductDetailListResponse;
import com.sparta.msa_exam.order.exception.CustomRuntimeException;
import com.sparta.msa_exam.order.exception.ExceptionMessage;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        return new ProductClient() {
            @Override
            public void fail() {
                throw new CustomRuntimeException(ExceptionMessage.PRODUCT_SERVICE_UNAVAILABLE);
            }

            @Override
            public ProductDetailListResponse checkProductsExist(ProductIdListRequest request) {
                throw new CustomRuntimeException(ExceptionMessage.PRODUCT_NOT_FOUND);
            }
        };
    }
}
