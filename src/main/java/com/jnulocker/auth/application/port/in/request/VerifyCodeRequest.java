package com.jnulocker.auth.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyCodeRequest(
        @NotBlank(message = "이메일은 필수 입력 값입니다") @Email(message = "올바른 형식의 이메일 주소여야 합니다")
                String email,
        @NotBlank(message = "인증 코드는 필수 입력 값입니다") String code) {
    @Schema(hidden = true)
    @AssertTrue(message = "인증 코드는 100000에서 999999 사이의 6자리 숫자여야 합니다")
    public boolean isCodeValid() {
        if (code == null) {
            return false;
        }
        try {
            int codeValue = Integer.parseInt(code);
            return codeValue >= 100000 && codeValue <= 999999;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
