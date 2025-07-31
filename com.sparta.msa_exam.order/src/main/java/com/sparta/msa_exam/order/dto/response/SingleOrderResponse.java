package com.sparta.msa_exam.order.dto.response;

import java.util.List;

public record SingleOrderResponse(
        Long orderId,
        List<SingleProductDetailResponse> products
) {
}
