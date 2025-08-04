package com.sparta.msa_exam.order.application;

import com.sparta.msa_exam.order.domain.dto.request.OrderCreateRequest;
import com.sparta.msa_exam.order.domain.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.order.domain.dto.request.SingleProductIdRequest;
import com.sparta.msa_exam.order.domain.dto.response.ProductDetailListResponse;
import com.sparta.msa_exam.order.domain.dto.response.SingleOrderResponse;
import com.sparta.msa_exam.order.domain.dto.response.SingleProductIdResponse;
import com.sparta.msa_exam.order.domain.entity.Order;
import com.sparta.msa_exam.order.common.exception.CustomRuntimeException;
import com.sparta.msa_exam.order.common.exception.ExceptionMessage;
import com.sparta.msa_exam.order.infra.jpa.OrderJpaRepository;
import com.sparta.msa_exam.order.infra.feign.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderJpaRepository orderJpaRepository;

    @Cacheable(value = "order", key = "#orderId") // order::{orderId}로 주문 정보 캐싱
    public SingleOrderResponse findOrder(Long orderId) {
        Order order = findOrderById(orderId);

        // 해당 주문에 포함되는 상품 ID 리스트 매핑
        List<SingleProductIdResponse> productIds = order.getOrderProducts().stream()
                .map(orderProduct -> new SingleProductIdResponse(orderProduct.getProductId()))
                .toList();

        return new SingleOrderResponse(order.getId(), productIds);
    }

    public SingleOrderResponse createOrder(OrderCreateRequest request, Boolean fail) {
        ProductIdListRequest productIdList = new ProductIdListRequest(request.productIds());

        // query parameter fail 값이 true인 경우 product-service unavailable 예외를 던져 서비스에 문제가 생김을 가정
        if (Boolean.TRUE.equals(fail)) {
            productClient.fail();
        }

        ProductDetailListResponse productDetailListResponse = productClient.validateProductIds(productIdList);

        Order order = new Order();

        request.productIds()
                .forEach(product -> order.addOrderProduct(product.productId()));

        orderJpaRepository.save(order);

        return new SingleOrderResponse(order.getId(), productDetailListResponse.productIds());
    }

    public SingleOrderResponse addSingleProduct(Long orderId, SingleProductIdRequest request) {
        Order order = findOrderById(orderId);

        productClient.validateProductIds(new ProductIdListRequest(
                List.of(new SingleProductIdRequest(request.productId())))
        );

        order.addOrderProduct(request.productId());

        // 해당 주문에 포함되는 상품 ID 리스트 매핑
        List<SingleProductIdResponse> productIds = order.getOrderProducts().stream()
                .map(orderProduct -> new SingleProductIdResponse(orderProduct.getProductId()))
                .toList();

        return new SingleOrderResponse(order.getId(), productIds);
    }

    private Order findOrderById(Long orderId) {
        return orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionMessage.ORDER_NOT_FOUND));
    }
}
