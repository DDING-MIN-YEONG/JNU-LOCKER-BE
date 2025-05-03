package com.jnulocker.auth.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ManagerApproveRequest(
        @Schema(description = "승인할 MANAGER ID", example = "1")
                @NotNull(message = "MANAGER ID는 필수입니다.")
                Long memberId) {}
