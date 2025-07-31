package com.sparta.msa_exam.product.dto.request;

import java.util.List;

public record ProductIdsRequest(
        List<SingleProductIdRequest> productIds
) {
}
