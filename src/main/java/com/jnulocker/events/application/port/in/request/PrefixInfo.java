package com.jnulocker.events.application.port.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record PrefixInfo(
        @Schema(description = "사물함 접두사 (선택사항, 기본값: 빈 문자열)", example = "A", nullable = true)
                @Pattern(
                        regexp = "^[A-Za-zㄱ-ㅣ가-힣0-9]*$",
                        message = "사물함 접두사는 영어, 한글, 숫자만 포함할 수 있습니다.")
                String lockerPrefix,
        @Schema(description = "사물함 번호 범위 목록")
                @NotEmpty(message = "사물함 번호 범위는 최소 1개 이상이어야 합니다.")
                @Valid
                List<LockerRange> ranges) {}
