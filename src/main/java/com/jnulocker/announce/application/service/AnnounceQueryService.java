package com.jnulocker.announce.application.service;

import com.jnulocker.announce.application.port.in.AnnounceQuery;
import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.AnnounceDetailResponse;
import com.jnulocker.announce.application.port.in.response.MyAnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.MyAnnounceDetailResponse;
import com.jnulocker.announce.application.port.out.AnnounceLoadPort;
import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.announce.exception.AnnounceNotFoundException;
import com.jnulocker.announce.exception.AnnounceParticipationNotFoundException;
import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.domain.Department;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnounceQueryService implements AnnounceQuery {

    private final MemberQuery memberQuery;
    private final AnnounceLoadPort announceLoadPort;

    @Override
    public AnnounceCustomPage getAnnounces(Pageable pageable) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdWithDepartmentOrThrow(memberId);

        Page<Announce> announces =
                announceLoadPort.getAnnouncesByDepartment(member.getDepartment(), pageable);
        return AnnounceCustomPage.from(announces);
    }

    @Override
    public MyAnnounceCustomPage getMyAnnounces(Pageable pageable) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdWithDepartmentOrThrow(memberId);

        // 사용자의 소속학과가 참여하는 공지사항 조회
        Page<Announce> announces =
                announceLoadPort.getAnnouncesByParticipationDepartment(
                        member.getDepartment(), pageable);

        return MyAnnounceCustomPage.from(announces);
    }

    @Override
    public Announce getByIdOrThrow(Long announceId) {
        return announceLoadPort
                .getById(announceId)
                .orElseThrow(() -> AnnounceNotFoundException.EXCEPTION);
    }

    @Override
    public Announce getByIdAndDepartmentOrThrow(Long announceId, Department department) {
        return announceLoadPort
                .getByIdAndDepartment(announceId, department)
                .orElseThrow(() -> AnnounceNotFoundException.EXCEPTION);
    }

    @Override
    public AnnounceParticipation getByAnnounceIdAndDepartmentOrThrow(
            Long announceId, Department department) {
        return announceLoadPort
                .getByAnnounceIdAndDepartment(announceId, department)
                .orElseThrow(() -> AnnounceParticipationNotFoundException.EXCEPTION);
    }

    @Override
    public AnnounceDetailResponse getAnnounce(Long announceId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        // Member가 주관하는 공지사항 조회
        Announce announce = getByIdAndDepartmentOrThrow(announceId, member.getDepartment());

        // 공지사항에 참여하는 소속학과 조회
        List<Department> participatingDepartments =
                announceLoadPort.getParticipationDepartments(announce);

        return AnnounceDetailResponse.from(announce, participatingDepartments);
    }

    @Override
    public MyAnnounceDetailResponse getMyAnnounce(Long announceId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        // Member가 참여하는 공지사항 조회
        AnnounceParticipation announceParticipation =
                getByAnnounceIdAndDepartmentOrThrow(announceId, member.getDepartment());

        return MyAnnounceDetailResponse.from(announceParticipation.getAnnounce());
    }
}
