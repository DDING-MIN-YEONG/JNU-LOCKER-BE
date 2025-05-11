package com.jnulocker.announce.application.service;

import com.jnulocker.announce.application.port.in.AnnounceCommand;
import com.jnulocker.announce.application.port.in.AnnounceQuery;
import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import com.jnulocker.announce.application.port.out.AnnounceRecordPort;
import com.jnulocker.announce.domain.Announce;
import com.jnulocker.announce.domain.AnnounceParticipation;
import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnounceCommandService implements AnnounceCommand {

    private final AnnounceRecordPort announceRecordPort;
    private final MemberQuery memberQuery;
    private final DepartmentQuery departmentQuery;
    private final AnnounceQuery announceQuery;

    @Override
    @Transactional
    public void createAnnounce(CreateAnnounceRequest request) {
        // 공지사항 생성 및 저장
        Announce savedAnnounce = createAndSaveAnnounce(request);

        // 공지사항 참여 학과 정보 생성 및 저장
        setupAnnounceParticipations(savedAnnounce, request.participationDepartmentIds());
    }

    @Override
    @Transactional
    public void deleteAnnounce(Long announceId) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        Announce announce = announceQuery.getByIdOrThrow(announceId);
        announce.validateDeletable(member.getDepartment());

        announceRecordPort.deleteAnnounce(announce);
    }

    private Announce createAndSaveAnnounce(CreateAnnounceRequest request) {
        // 공지사항을 게시하는 department 조회
        Long memberId = SecurityUtils.getCurrentMemberId();
        Department organizerDepartment =
                memberQuery.findByIdWithDepartmentOrThrow(memberId).getDepartment();

        // 공지사항 생성 및 저장
        return announceRecordPort.saveAnnounce(
                Announce.create(
                        request.title(),
                        request.content(),
                        organizerDepartment.getNickname(),
                        organizerDepartment));
    }

    private void setupAnnounceParticipations(
            Announce announce, List<Long> participationDepartmentIds) {
        // 공지사항에 참여하는 학과 조회
        List<Department> departments =
                departmentQuery.getDepartmentsByIdIn(participationDepartmentIds);
        if (departments.size() != participationDepartmentIds.size()) {
            throw DepartmentNotFoundException.EXCEPTION;
        }

        // 공지사항 참여 정보 생성 및 저장
        List<AnnounceParticipation> participations =
                departments.stream()
                        .map(department -> AnnounceParticipation.create(announce, department))
                        .toList();
        announceRecordPort.saveAnnounceParticipations(participations);
    }
}
