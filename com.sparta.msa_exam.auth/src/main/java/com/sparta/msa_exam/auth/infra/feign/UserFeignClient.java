package com.sparta.msa_exam.auth.infra.feign;


import com.sparta.msa_exam.auth.domain.dto.request.UserCreateRequest;
import com.sparta.msa_exam.auth.domain.dto.response.UserAccountResponse;
import com.sparta.msa_exam.auth.domain.dto.response.UserCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @PostMapping("/feign/users")
    UserCreateResponse createUser(@RequestBody UserCreateRequest userCreateRequest);

    @GetMapping("/feign/users")
    UserAccountResponse findUserByIdOrdUsername(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String username
    );
}
