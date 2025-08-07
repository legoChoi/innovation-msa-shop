package com.sparta.msa_exam.auth.infra.feign;

import com.sparta.msa_exam.auth.common.exception.CustomRuntimeException;
import com.sparta.msa_exam.auth.common.exception.ExceptionMessage;
import com.sparta.msa_exam.auth.domain.dto.request.UserCreateRequest;
import com.sparta.msa_exam.auth.domain.dto.response.UserAccountResponse;
import com.sparta.msa_exam.auth.domain.dto.response.UserCreateResponse;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        return new UserFeignClient() {
            @Override
            public UserCreateResponse createUser(UserCreateRequest userCreateRequest) {
                if (cause instanceof FeignException.Conflict) {
                    throw new CustomRuntimeException(ExceptionMessage.DUPLICATED_USERNAME);
                }

                if (cause instanceof FeignException.NotFound) {
                    throw new CustomRuntimeException(ExceptionMessage.USER_NOT_FOUND);
                }

                throw new CustomRuntimeException(ExceptionMessage.USER_SERVICE_UNAVAILABLE);
            }

            @Override
            public UserAccountResponse findUserByIdOrdUsername(Long userId, String username) {
                if (cause instanceof FeignException.NotFound) {
                    throw new CustomRuntimeException(ExceptionMessage.USER_NOT_FOUND);
                }

                throw new CustomRuntimeException(ExceptionMessage.USER_SERVICE_UNAVAILABLE);
            }
        };
    }
}
