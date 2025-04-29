package com.jnulocker.registration.application.port.in.response;

import com.jnulocker.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegistrationMemberInfo(
        @Schema(description = "신청자 이름", example = "심민보") String name,
        @Schema(description = "신청자 학번", example = "20231234") String studentNumber,
        @Schema(description = "신청자 소속학과", example = "컴퓨터정보통신공학과") String department,
        @Schema(description = "신청자 이메일", example = "asdf123@example.com") String email) {
    public static RegistrationMemberInfo from(Member member) {
        return new RegistrationMemberInfo(
                member.getName(),
                member.getStudentNumber(),
                member.getDepartment().getName(),
                member.getEmail());
    }
}
