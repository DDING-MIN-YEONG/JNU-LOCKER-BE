package com.jnulocker.events.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record FloorInfo(
        @Schema(description = "층 번호", example = "1") @NotNull(message = "층 번호는 필수입니다.")
                Integer floorNumber,
        @Schema(description = "접두사 목록") @NotEmpty(message = "접두사 정보는 최소 1개 이상이어야 합니다.") @Valid
                List<PrefixInfo> prefixes) {}
