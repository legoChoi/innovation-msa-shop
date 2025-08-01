package com.sparta.msa_exam.order.repository;

import com.sparta.msa_exam.order.exception.CustomRuntimeException;
import com.sparta.msa_exam.order.exception.ExceptionMessage;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        return request -> {
            throw new CustomRuntimeException(ExceptionMessage.PRODUCT_NOT_FOUND);
        };
    }
}
