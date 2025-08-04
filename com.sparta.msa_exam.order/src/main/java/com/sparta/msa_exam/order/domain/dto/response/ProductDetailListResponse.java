package com.sparta.msa_exam.order.domain.dto.response;

import java.util.List;

public record ProductDetailListResponse(
        List<SingleProductIdResponse> productIds
) {
}
