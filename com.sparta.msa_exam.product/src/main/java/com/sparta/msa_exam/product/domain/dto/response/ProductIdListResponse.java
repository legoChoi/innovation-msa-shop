package com.sparta.msa_exam.product.domain.dto.response;

import java.util.List;

public record ProductIdListResponse(
        List<SingleProductIdResponse> productIds
) {
}
