package com.sparta.msa_exam.user.application;

import com.sparta.msa_exam.user.common.exception.CustomRuntimeException;
import com.sparta.msa_exam.user.common.exception.ExceptionMessage;
import com.sparta.msa_exam.user.domain.dto.request.UserCreateRequest;
import com.sparta.msa_exam.user.domain.dto.response.UserAccountResponse;
import com.sparta.msa_exam.user.domain.dto.response.UserCreateResponse;
import com.sparta.msa_exam.user.domain.entity.User;
import com.sparta.msa_exam.user.infra.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInternalService {

    private final UserJpaRepository userJpaRepository;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        checkDuplicatedUserByUsername(request.username());

        User user = new User(request.username(), request.password());
        userJpaRepository.save(user);

        return new UserCreateResponse(user.getId());
    }

    public UserAccountResponse findUserBy(Long userId, String username) {
        User user = Optional.ofNullable(userId)
                .map(this::findById)
                .orElseGet(() -> Optional.ofNullable(username)
                        .map(this::findByUsername)
                        .orElseThrow(() -> new CustomRuntimeException(ExceptionMessage.USER_NOT_FOUND)));

        return new UserAccountResponse(user.getId(), user.getUsername(), user.getPassword());
    }

    private User findById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionMessage.USER_NOT_FOUND));
    }

    private User findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionMessage.USER_NOT_FOUND));
    }

    private void checkDuplicatedUserByUsername(String username) {
        if (userJpaRepository.existsByUsername(username)) {
            throw new CustomRuntimeException(ExceptionMessage.DUPLICATED_USERNAME);
        }
    }
}
