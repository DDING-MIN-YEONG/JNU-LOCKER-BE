package com.jnulocker.registration.application.port.in.response;

import com.jnulocker.registration.domain.Registration;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegistrationResponse(
        @Schema(description = "신청 ID", example = "1") Long id,
        @Schema(description = "층 번호", example = "2") Integer floorNumber,
        @Schema(description = "사물함 번호", example = "A-021") String lockerCode) {

    public static RegistrationResponse from(Registration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getLocker().getFloor().getFloorNumber(),
                registration.getLocker().getCode());
    }
}
