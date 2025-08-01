package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.dto.request.OrderCreateRequest;
import com.sparta.msa_exam.order.dto.request.SingleProductIdRequest;
import com.sparta.msa_exam.order.dto.response.SingleOrderResponse;
import com.sparta.msa_exam.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<SingleOrderResponse> findOrder(
            @PathVariable Long orderId
    ) {
        SingleOrderResponse response = orderService.findOrder(orderId);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    public ResponseEntity<SingleOrderResponse> createOrder(
            @RequestBody OrderCreateRequest orderCreateRequest,
            @RequestParam(required = false) Boolean fail
    ) {
        SingleOrderResponse response = orderService.createOrder(orderCreateRequest, fail);

        return ResponseEntity.created(null)
                .body(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<SingleOrderResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid SingleProductIdRequest singleProductIdRequest
    ) {
        SingleOrderResponse response = orderService.addSingleProduct(orderId, singleProductIdRequest);

        return ResponseEntity.created(null)
                .body(response);
    }
}
