package com.jnulocker.auth.application.port.in.response;

import com.jnulocker.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

public record PendingManagerCustomPage(
        @Schema(description = "대기 중인 관리자 목록") List<PendingManagerListItem> content,
        @Schema(description = "전체 대기 관리자 수", example = "10") Long totalElements,
        @Schema(description = "마지막 페이지 여부", example = "false") Boolean last) {
    public static PendingManagerCustomPage from(Page<Member> pendingManagers) {
        return new PendingManagerCustomPage(
                pendingManagers.getContent().stream().map(PendingManagerListItem::from).toList(),
                pendingManagers.getTotalElements(),
                pendingManagers.isLast());
    }
}
