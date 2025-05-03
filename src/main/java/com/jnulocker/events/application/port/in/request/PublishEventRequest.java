package com.jnulocker.events.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PublishEventRequest(
        @Schema(description = "이벤트 publish 여부", example = "true")
                @NotNull(message = "이벤트 publish 여부는 필수입니다.")
                Boolean isPublish) {}
