package com.jnulocker.events.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record LockerRange(
        @Schema(description = "사물함 시작 번호", example = "1")
                @NotNull(message = "사물함 시작 번호는 필수입니다.")
                @PositiveOrZero(message = "사물함 시작 번호는 0 이상이어야 합니다.")
                Integer lockerStartNumber,
        @Schema(description = "사물함 종료 번호", example = "20")
                @NotNull(message = "사물함 종료 번호는 필수입니다.")
                @PositiveOrZero(message = "사물함 종료 번호는 0 이상이어야 합니다.")
                Integer lockerEndNumber) {

    @AssertTrue(message = "사물함 종료 번호는 시작 번호보다 크거나 같아야 합니다.")
    private boolean isEndNumberGreaterThanStartNumber() {
        // null 체크: @NotNull에 의해 처리되므로, null이면 검증 스킵
        if (lockerStartNumber == null || lockerEndNumber == null) {
            return true;
        }
        return lockerEndNumber >= lockerStartNumber;
    }
}
