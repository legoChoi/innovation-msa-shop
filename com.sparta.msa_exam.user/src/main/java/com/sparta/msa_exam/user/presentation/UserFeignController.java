package com.sparta.msa_exam.user.presentation;

import com.sparta.msa_exam.user.application.UserInternalService;
import com.sparta.msa_exam.user.domain.dto.request.UserCreateRequest;
import com.sparta.msa_exam.user.domain.dto.response.UserAccountResponse;
import com.sparta.msa_exam.user.domain.dto.response.UserCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feign/users")
@RequiredArgsConstructor
public class UserFeignController {

    private final UserInternalService userInternalService;

    @PostMapping
    public ResponseEntity<UserCreateResponse> createUser(
            @RequestBody @Valid UserCreateRequest userCreateRequest
    ) {
        UserCreateResponse response = userInternalService.createUser(userCreateRequest);

        return ResponseEntity.created(null)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<UserAccountResponse> findUserByIdOrdUsername(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String username
    ) {
        UserAccountResponse response = userInternalService.findUserBy(userId, username);

        return ResponseEntity.ok()
                .body(response);
    }
}
