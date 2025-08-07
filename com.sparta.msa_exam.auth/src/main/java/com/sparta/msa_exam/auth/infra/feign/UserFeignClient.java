package com.sparta.msa_exam.auth.infra.feign;


import com.sparta.msa_exam.auth.domain.dto.response.UserAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/feign/users")
    UserAccountResponse findUserByIdOrdUsername(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String username
    );
}
