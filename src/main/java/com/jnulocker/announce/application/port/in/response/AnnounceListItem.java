package com.jnulocker.announce.application.port.in.response;

import com.jnulocker.announce.domain.Announce;
import io.swagger.v3.oas.annotations.media.Schema;

public record AnnounceListItem(
        @Schema(description = "공지사항 ID", example = "1") Long id,
        @Schema(description = "공지사항 제목", example = "백도 사물함 신청 안내") String title,
        @Schema(description = "공지사항 내용", example = "공지사항 내용입니다.") String content,
        @Schema(description = "공지사항 작성자", example = "도서관자치위원회 백문이불여일견") String writer) {
    public static AnnounceListItem from(Announce announce) {
        return new AnnounceListItem(
                announce.getId(), announce.getTitle(), announce.getContent(), announce.getWriter());
    }
}
