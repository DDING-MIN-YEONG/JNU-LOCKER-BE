package com.jnulocker.auth.application.port.in.response;

import com.jnulocker.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record PendingManagerListItem(
        @Schema(description = "회원 ID", example = "1") Long memberId,
        @Schema(description = "학번", example = "225312") String studentNumber,
        @Schema(description = "이름", example = "강명덕") String name,
        @Schema(description = "가입일", example = "2025-08-01T15:00:00") LocalDateTime createdAt,
        @Schema(description = "이메일", example = "test123@example.com") String email) {

    public static PendingManagerListItem from(Member member) {
        return new PendingManagerListItem(
                member.getId(),
                member.getStudentNumber(),
                member.getName(),
                member.getCreatedAt(),
                member.getEmail());
    }
}
