package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

public record EventCustomPage(
        @Schema(description = "이벤트 목록") List<EventListItem> content,
        @Schema(description = "이벤트 전체 개수", example = "10") Long totalElements,
        @Schema(description = "마지막 페이지 여부", example = "false") Boolean last) {

    public static EventCustomPage from(Page<Event> events) {
        return new EventCustomPage(
                events.getContent().stream().map(EventListItem::from).toList(),
                events.getTotalElements(),
                events.isLast());
    }
}
