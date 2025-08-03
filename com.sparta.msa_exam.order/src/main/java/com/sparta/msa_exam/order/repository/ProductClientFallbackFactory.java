package com.sparta.msa_exam.order.repository;

import com.sparta.msa_exam.order.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.order.dto.response.ProductDetailListResponse;
import com.sparta.msa_exam.order.exception.CustomRuntimeException;
import com.sparta.msa_exam.order.exception.ExceptionMessage;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        return new ProductClient() {
            @Override
            public void fail() {
                logging(ExceptionMessage.PRODUCT_SERVICE_UNAVAILABLE);
                throw new CustomRuntimeException(ExceptionMessage.PRODUCT_SERVICE_UNAVAILABLE);
            }

            @Override
            public ProductDetailListResponse checkProductsExist(ProductIdListRequest request) {
                if (cause instanceof FeignException.NotFound) {
                    logging(ExceptionMessage.PRODUCT_NOT_FOUND);
                    throw new CustomRuntimeException(ExceptionMessage.PRODUCT_NOT_FOUND);
                }

                log.error("Unknown exception caught");
                throw new CustomRuntimeException(ExceptionMessage.PRODUCT_SERVICE_UNAVAILABLE);
            }
        };
    }

    private void logging(ExceptionMessage exception) {
        log.error("[Order-Service][{}][{}]",
                exception.getStatus(),
                exception.getMessage()
        );
    }
}
