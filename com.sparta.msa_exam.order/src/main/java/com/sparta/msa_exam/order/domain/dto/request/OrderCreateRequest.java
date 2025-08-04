package com.sparta.msa_exam.order.domain.dto.request;

import java.util.List;

public record OrderCreateRequest(
        List<SingleProductIdRequest> productIds
) {
}
