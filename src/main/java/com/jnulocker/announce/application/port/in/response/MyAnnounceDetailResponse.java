package com.jnulocker.announce.application.port.in.response;

import com.jnulocker.announce.domain.Announce;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record MyAnnounceDetailResponse(
        @Schema(description = "공지사항 ID", example = "1") Long id,
        @Schema(description = "공지사항 제목", example = "백도 사물함 신청 안내") String title,
        @Schema(description = "공지사항 내용", example = "공지사항 내용입니다.") String content,
        @Schema(description = "공지사항 작성자", example = "도서관자치위원회 백문이불여일견") String writer,
        @Schema(description = "등록 일자", example = "2025-08-01T15:00:00") LocalDateTime createdAt,
        @Schema(description = "수정 일자", example = "2025-08-10T15:00:00") LocalDateTime updatedAt) {
    public static MyAnnounceDetailResponse from(Announce announce) {
        return new MyAnnounceDetailResponse(
                announce.getId(),
                announce.getTitle(),
                announce.getContent(),
                announce.getWriter(),
                announce.getCreatedAt(),
                announce.getUpdatedAt());
    }
}
