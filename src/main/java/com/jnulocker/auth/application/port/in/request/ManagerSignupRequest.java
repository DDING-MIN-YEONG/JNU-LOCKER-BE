package com.jnulocker.auth.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ManagerSignupRequest(
        @Schema(description = "소속 이름", example = "디자인학과 학생회 아트")
                @NotBlank(message = "소속 이름은 필수입니다.")
                String nickName,
        @Schema(description = "이메일", example = "design@gmail.com")
                @NotBlank(message = "이메일은 필수입니다.")
                @Email(message = "올바른 이메일 형식이 아닙니다.")
                String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(
                        regexp =
                                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{9,}$",
                        message = "비밀번호는 영문, 숫자, 특수문자를 각각 최소 1자 이상 포함하고 9자 이상이어야 합니다.")
                String password,
        @Schema(description = "소속학과", example = "128") @NotNull(message = "소속은 필수입니다.")
                Long departmentId,
        @NotBlank(message = "전화번호는 필수입니다.")
                @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
                String phoneNumber) {}
