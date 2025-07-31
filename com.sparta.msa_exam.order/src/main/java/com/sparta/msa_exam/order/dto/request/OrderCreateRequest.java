package com.sparta.msa_exam.order.dto.request;

import java.util.List;

public record OrderCreateRequest(
        List<OrderCreateSingleProductRequest> productIds
) {
}
