package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.request.OrderCreateRequest;
import com.sparta.msa_exam.order.dto.request.ProductIdListRequest;
import com.sparta.msa_exam.order.dto.response.ProductDetailListResponse;
import com.sparta.msa_exam.order.dto.response.SingleOrderResponse;
import com.sparta.msa_exam.order.dto.response.SingleProductIdResponse;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.repository.OrderRepository;
import com.sparta.msa_exam.order.repository.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public SingleOrderResponse findOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow();// TODO throw Order NotFound exception

        List<SingleProductIdResponse> productIds = order.getOrderProducts().stream()
                .map(orderProduct -> new SingleProductIdResponse(orderProduct.getProductId()))
                .toList();

        return new SingleOrderResponse(order.getId(), productIds);
    }

    public SingleOrderResponse createOrder(OrderCreateRequest request) {
        ProductDetailListResponse productDetailListResponse
                = productClient.isExistProducts(new ProductIdListRequest(request.productIds()));

        Order order = new Order();

        request.productIds()
                .forEach(product -> order.addOrderProduct(product.productId()));

        orderRepository.save(order);

        return new SingleOrderResponse(order.getId(), productDetailListResponse.productIds());
    }
}
