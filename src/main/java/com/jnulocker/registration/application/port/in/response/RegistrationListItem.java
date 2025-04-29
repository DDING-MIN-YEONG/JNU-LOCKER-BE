package com.jnulocker.registration.application.port.in.response;

import com.jnulocker.registration.domain.Registration;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegistrationListItem(
        @Schema(description = "신청 ID", example = "1") Long id,
        @Schema(description = "층 번호", example = "2") Integer floorNumber,
        @Schema(description = "사물함 번호", example = "A-021") String lockerCode,
        @Schema(description = "신청자 정보") RegistrationMemberInfo member) {

    public static RegistrationListItem from(Registration registration) {
        return new RegistrationListItem(
                registration.getId(),
                registration.getLocker().getFloor().getFloorNumber(),
                registration.getLocker().getCode(),
                RegistrationMemberInfo.from(registration.getMember()));
    }
}
