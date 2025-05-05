package com.jnulocker.member.adapter.in;

import com.jnulocker.member.adapter.in.docs.MemberApi;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberQuery memberQuery;

    @Override
    @GetMapping("/info")
    public ResponseEntity<MemberInfoResponse> getMemberInfo() {
        return ResponseEntity.ok(memberQuery.getMemberInfo());
    }
}
