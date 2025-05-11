package com.jnulocker.announce.application.service;

import com.jnulocker.announce.application.port.in.AnnounceQuery;
import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.MyAnnounceResponse;
import com.jnulocker.announce.application.port.out.AnnounceLoadPort;
import com.jnulocker.announce.domain.Announce;
import com.jnulocker.auth.security.SecurityUtils;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnnounceQueryService implements AnnounceQuery {

    private final MemberQuery memberQuery;
    private final AnnounceLoadPort announceLoadPort;

    @Override
    public AnnounceCustomPage getAllAnnounces(Pageable pageable) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdWithDepartmentOrThrow(memberId);

        Page<Announce> announces =
                announceLoadPort.getAllAnnouncesByDepartment(member.getDepartment(), pageable);
        return AnnounceCustomPage.from(announces);
    }

    @Override
    public List<MyAnnounceResponse> getMyAnnounces() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberQuery.findByIdOrThrow(memberId);

        // 사용자의 소속학과가 참여하는 공지사항 조회
        List<Announce> announces =
                announceLoadPort.getAnnouncesByParticipationDepartment(member.getDepartment());

        return announces.stream().map(MyAnnounceResponse::from).toList();
    }
}
