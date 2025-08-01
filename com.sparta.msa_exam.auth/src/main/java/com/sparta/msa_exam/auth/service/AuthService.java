package com.sparta.msa_exam.auth.service;

import com.sparta.msa_exam.auth.dto.request.AuthSignInRequest;
import com.sparta.msa_exam.auth.dto.request.AuthSignUpRequest;
import com.sparta.msa_exam.auth.dto.response.AuthSignInResponse;
import com.sparta.msa_exam.auth.dto.response.AuthSignUpResponse;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.repository.AuthRedisRepository;
import com.sparta.msa_exam.auth.repository.AuthRepository;
import com.sparta.msa_exam.auth.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final AuthRedisRepository authRedisRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthSignInResponse signIn(AuthSignInRequest authSignInRequest) {
        User user = authRepository.findByUsername(authSignInRequest.username())
                .orElseThrow();// TODO throw User NotFound Exception

        if (!passwordEncoder.matches(authSignInRequest.password(), user.getPassword())) {
            // TODO throw Password Mismatch Exception
        }

        String accessToken = jwtProvider.generateAccessToken(user.getId().toString());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId().toString());

        authRedisRepository.setRefreshToken(user.getId(), refreshToken);

        return new AuthSignInResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthSignUpResponse signUp(AuthSignUpRequest authSignUpRequest) {
        if (authRepository.existsByUsername(authSignUpRequest.username())) {
            // TODO throw Duplicated Username Exception
        }

        String encodedPassword = passwordEncoder.encode(authSignUpRequest.password());

        User user = new User(authSignUpRequest.username(), encodedPassword);
        authRepository.save(user);

        String accessToken = jwtProvider.generateAccessToken(user.getId().toString());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId().toString());

        authRedisRepository.setRefreshToken(user.getId(), refreshToken);

        return new AuthSignUpResponse(accessToken, refreshToken);
    }
}
