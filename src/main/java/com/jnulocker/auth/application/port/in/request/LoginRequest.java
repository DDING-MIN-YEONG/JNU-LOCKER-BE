package com.jnulocker.auth.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "이메일", example = "abcde@example.com")
                @NotBlank(message = "이메일은 필수 입력값입니다")
                @Email
                String email,
        @Schema(description = "비밀번호", example = "abcde12345!")
                @NotBlank(message = "비밀번호는 필수 입력값입니다")
                String password) {}
