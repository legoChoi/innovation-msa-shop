package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.request.OrderCreateRequest;
import com.sparta.msa_exam.order.dto.response.OrderCreateResponse;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(
            @RequestBody OrderCreateRequest orderCreateRequest
    ) {
        OrderCreateResponse response = orderService.createOrder(orderCreateRequest);

        return ResponseEntity.created(null)
                .body(response);
    }
}
