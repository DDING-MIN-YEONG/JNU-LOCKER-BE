package com.jnulocker.auth.application.port.in.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyCodeRequest(
        @NotBlank(message = "이메일은 필수 입력 값입니다") @Email(message = "올바른 형식의 이메일 주소여야 합니다")
                String email,
        String code) {}
