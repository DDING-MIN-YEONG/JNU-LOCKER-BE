package com.jnulocker.auth.adapter.in.request;

import com.jnulocker.member.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserSignupRequest(
        @NotBlank(message = "이메일은 필수입니다.")
                @Email(message = "올바른 이메일 형식이 아닙니다.")
                @Pattern(regexp = "^[A-Za-z0-9._%+-]+@jnu\\.ac\\.kr$", message = "전남대학교 메일이 아닙니다.")
                String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(
                        regexp =
                                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{9,}$",
                        message = "비밀번호는 영문, 숫자, 특수문자를 각각 최소 1자 이상 포함하고 9자 이상이어야 합니다.")
                String password,
        @NotNull(message = "소속은 필수입니다.") Long affiliationId,
        @NotNull(message = "소속은 필수입니다.") Long departmentId,
        @NotBlank(message = "전화번호는 필수입니다.")
                @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
                String phoneNumber,
        @NotNull(message = "역할은 필수입니다.") Role role) {}
