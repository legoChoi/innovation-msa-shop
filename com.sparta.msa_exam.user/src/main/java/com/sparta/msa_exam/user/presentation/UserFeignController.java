package com.sparta.msa_exam.user.presentation;

import com.sparta.msa_exam.user.application.UserInternalService;
import com.sparta.msa_exam.user.domain.dto.response.UserAccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feign/users")
@RequiredArgsConstructor
public class UserFeignController {

    private final UserInternalService userInternalService;

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
