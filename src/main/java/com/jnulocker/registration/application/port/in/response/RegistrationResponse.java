package com.jnulocker.registration.application.port.in.response;

import com.jnulocker.registration.domain.Registration;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegistrationResponse(
        @Schema(description = "신청 ID", example = "1") Long id,
        @Schema(description = "사물함 번호", example = "A-021") String lockerCode,
        @Schema(description = "신청자 자신의 소속학과", example = "컴퓨터정보통신공학과") String department) {

    public static RegistrationResponse from(Registration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getLocker().getCode(),
                registration.getMember().getDepartment().getName());
    }
}
