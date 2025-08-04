package com.sparta.msa_exam.order.domain.dto.response;

import java.util.List;

public record SingleOrderResponse(
        Long orderId,
        List<SingleProductIdResponse> productIds
) {
}
