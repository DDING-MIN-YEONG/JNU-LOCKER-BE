package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record EventDepartmentResponse(
        @Schema(description = "학과 ID", example = "1") Long id,
        @Schema(description = "학과 이름", example = "전자컴퓨터공학부") String name) {}
