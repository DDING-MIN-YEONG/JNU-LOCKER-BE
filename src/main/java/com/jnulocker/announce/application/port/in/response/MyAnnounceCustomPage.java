package com.jnulocker.announce.application.port.in.response;

import com.jnulocker.announce.domain.Announce;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

public record MyAnnounceCustomPage(
        @Schema(description = "공지사항 목록") List<AnnounceListItem> content,
        @Schema(description = "공지사항 전체 개수", example = "10") Long totalElements,
        @Schema(description = "마지막 페이지 여부", example = "false") Boolean last) {
    public static MyAnnounceCustomPage from(Page<Announce> announces) {
        return new MyAnnounceCustomPage(
                announces.getContent().stream().map(AnnounceListItem::from).toList(),
                announces.getTotalElements(),
                announces.isLast());
    }
}
