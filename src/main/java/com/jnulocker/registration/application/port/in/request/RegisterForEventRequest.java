package com.jnulocker.registration.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegisterForEventRequest(
        @Schema(description = "사물함 번호", example = "1")
        @NotNull(message = "사물함 번호는 필수입니다.")
        @Min(value = 1, message = "사물함 번호는 1 이상이어야 합니다.")
        Long lockerId) {}
