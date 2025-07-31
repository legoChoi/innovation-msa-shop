package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.request.OrderCreateRequest;
import com.sparta.msa_exam.order.dto.response.OrderCreateResponse;
import com.sparta.msa_exam.order.dto.response.ProductListResponse;
import com.sparta.msa_exam.order.entity.Order;
import com.sparta.msa_exam.order.repository.OrderRepository;
import com.sparta.msa_exam.order.repository.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public OrderCreateResponse createOrder(OrderCreateRequest request) {
        ProductListResponse productListResponse = productClient.checkAndFindAllProducts(request);

        log.info(productListResponse.toString());

        Order order = new Order();

        request.productIds()
                .forEach(product -> order.addOrderProduct(product.productId()));

        orderRepository.save(order);

        return new OrderCreateResponse(order.getId(), productListResponse.products());
    }
}
