package com.jnulocker.auth.application.port.in.request;

import com.jnulocker.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserSignupRequest(
        @Schema(description = "이름", example = "서영우") String name,
        @Schema(description = "전남대학교 학생 계정 메일", example = "222222@jnu.ac.kr")
                @NotBlank(message = "이메일은 필수입니다.")
                @Email(message = "올바른 이메일 형식이 아닙니다.")
                @Pattern(regexp = "^[A-Za-z0-9._%+-]+@jnu\\.ac\\.kr$", message = "전남대학교 메일이 아닙니다.")
                String email,
        @Schema(description = "비밀번호", example = "abcde12345!")
                @NotBlank(message = "비밀번호는 필수입니다.")
                @Pattern(
                        regexp =
                                "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{9,}$",
                        message = "비밀번호는 영문, 숫자, 특수문자를 각각 최소 1자 이상 포함하고 9자 이상이어야 합니다.")
                String password,
        @Schema(description = "소속대학", example = "자연과학대학") @NotNull(message = "소속은 필수입니다.")
                Long organizationId,
        @Schema(description = "소속학과", example = "수학과") @NotNull(message = "소속은 필수입니다.")
                Long departmentId,
        @Schema(description = "전화번호", example = "010-1234-1234")
                @NotBlank(message = "전화번호는 필수입니다.")
                @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
                String phoneNumber,
        @Schema(description = "ROLE", example = "USER") @NotNull(message = "역할은 필수입니다.")
                Role role) {}
