package com.jnulocker.member.application.port.in.response;

import com.jnulocker.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfoResponse(
        @Schema(description = "이름", example = "심민보") String name,
        @Schema(description = "학번", example = "222222") String studentNumber,
        @Schema(description = "소속", example = "수학과") String affiliation,
        @Schema(description = "전화번호", example = "010-1234-1234") String phoneNumber,
        @Schema(description = "이메일", example = "test@example.com") String email) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getName(),
                member.getStudentNumber(),
                member.getDepartment().getName(),
                member.getPhoneNumber(),
                member.getEmail());
    }
}
