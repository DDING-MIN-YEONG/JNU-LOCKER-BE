package com.jnulocker.registration.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RegisterForEventRequest(
        @Schema(description = "사물함 번호", example = "550e8400-e29b-41d4-a716-446655440000")
                @NotNull(message = "사물함 번호는 필수입니다.")
                UUID lockerId) {}
