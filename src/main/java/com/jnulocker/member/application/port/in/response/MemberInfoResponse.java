package com.jnulocker.member.application.port.in.response;

import com.jnulocker.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfoResponse(
        @Schema(description = "회원 ID", example = "1") Long memberId,
        @Schema(description = "이름", example = "심민보") String name,
        @Schema(description = "학번", example = "222222") String studentNumber,
        @Schema(description = "소속", example = "수학과") String affiliation,
        @Schema(description = "전화번호", example = "010-1234-1234") String phoneNumber,
        @Schema(description = "이메일", example = "test@example.com") String email,
        @Schema(description = "회원 권한", example = "USER") Role role) {
    public static MemberInfoResponse of(
            Long memberId,
            String name,
            String studentNumber,
            String affiliation,
            String phoneNumber,
            String email,
            Role role) {
        return new MemberInfoResponse(
                memberId, name, studentNumber, affiliation, phoneNumber, email, role);
    }
}
