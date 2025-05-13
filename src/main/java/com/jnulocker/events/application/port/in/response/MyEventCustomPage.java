package com.jnulocker.events.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MyEventCustomPage(
        @Schema(description = "이벤트 목록") List<MyEventListItem> content,
        @Schema(description = "이벤트 전체 개수", example = "10") Long totalElements,
        @Schema(description = "마지막 페이지 여부", example = "false") Boolean last) {

    public static MyEventCustomPage of(
            List<MyEventListItem> content, Long totalElements, Boolean last) {
        return new MyEventCustomPage(content, totalElements, last);
    }
}
