package com.jnulocker.announce.application.port.in.response;

import com.jnulocker.announce.domain.Announce;
import com.jnulocker.organization.domain.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

public record AnnounceDetailResponse(
        @Schema(description = "공지사항 ID", example = "1") Long id,
        @Schema(description = "공지사항 제목", example = "백도 사물함 신청 안내") String title,
        @Schema(description = "공지사항 내용", example = "공지사항 내용입니다.") String content,
        @Schema(description = "공지사항 작성자", example = "도서관자치위원회 백문이불여일견") String writer,
        @Schema(description = "등록 일자", example = "2025-08-01T15:00:00") LocalDateTime createdAt,
        @Schema(description = "수정 일자", example = "2025-08-10T15:00:00") LocalDateTime updatedAt,
        @Schema(description = "참여 학과 목록") List<DepartmentResponse> departments) {
    public static AnnounceDetailResponse of(Announce announce, List<Department> departments) {
        List<DepartmentResponse> departmentResponses =
                departments.stream().map(DepartmentResponse::from).toList();
        return new AnnounceDetailResponse(
                announce.getId(),
                announce.getTitle(),
                announce.getContent(),
                announce.getWriter(),
                announce.getCreatedAt(),
                announce.getUpdatedAt(),
                departmentResponses);
    }
}
