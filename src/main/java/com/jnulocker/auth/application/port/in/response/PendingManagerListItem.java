package com.jnulocker.auth.application.port.in.response;

import com.jnulocker.member.domain.Member;
import java.time.LocalDateTime;

public record PendingManagerListItem(
        Long memberId, String studentNumber, String name, LocalDateTime createdAt, String email) {

    public static PendingManagerListItem from(Member member) {
        return new PendingManagerListItem(
                member.getId(),
                member.getStudentNumber(),
                member.getName(),
                member.getCreatedAt(),
                member.getEmail());
    }
}
