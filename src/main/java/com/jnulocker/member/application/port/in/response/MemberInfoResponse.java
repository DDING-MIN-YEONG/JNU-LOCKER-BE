package com.jnulocker.member.application.port.in.response;

import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.organization.domain.OrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfoResponse(
        @Schema(description = "회원 ID", example = "1") Long memberId,
        @Schema(description = "이름", example = "심민보") String name,
        @Schema(description = "학번", example = "222222") String studentNumber,
        @Schema(description = "소속", example = "수학과") String affiliation,
        @Schema(description = "전화번호", example = "010-1234-1234") String phoneNumber,
        @Schema(description = "이메일", example = "test@example.com") String email) {
    public static MemberInfoResponse from(Member member) {
        String studentNumber = null;
        String affiliation = null;

        Role role = member.getRole();
        OrganizationType orgType = member.getDepartment().getOrganization().getType();

        if (role == Role.USER) {
            studentNumber = member.getStudentNumber();
            affiliation = member.getDepartment().getName();
        } else if (role == Role.MANAGER) { // 쉽게 테스트하려면 GUEST로 권한 바꾸기
            if (orgType == OrganizationType.COUNCIL) {
                studentNumber = member.getStudentNumber();
            }
            affiliation = member.getDepartment().getNickname();
        }

        return new MemberInfoResponse(
                member.getId(),
                member.getName(),
                studentNumber,
                affiliation,
                member.getPhoneNumber(),
                member.getEmail());
    }
}
