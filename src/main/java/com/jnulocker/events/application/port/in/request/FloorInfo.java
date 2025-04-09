package com.jnulocker.events.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FloorInfo(
        @Schema(description = "층 번호", example = "1") @NotNull(message = "층 번호는 필수입니다.")
                Integer floorNumber,
        @Schema(description = "사물함 접두사(선택 사항)", example = "A") String lockerPrefix,
        @Schema(description = "사물함 시작 번호", example = "1") @NotNull(message = "사물함 시작 번호는 필수입니다.")
                Integer lockerStartNumber,
        @Schema(description = "사물함 종료 번호", example = "20") @NotNull(message = "사물함 종료 번호는 필수입니다.")
                Integer lockerEndNumber) {}
