package com.jnulocker.ai.application.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberTools {

    private final MemberQuery memberQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "현재 로그인한 사용자의 정보를 조회합니다. 이름, 이메일, 소속 학과, 학번 등을 확인할 수 있습니다.")
    @AiToolMethod
    public String getMyInfo() throws JsonProcessingException {
        MemberInfoResponse info = memberQuery.getMemberInfo();
        return objectMapper.writeValueAsString(info);
    }
}
