package com.jnulocker.ai.application.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberTools {

    private final MemberQuery memberQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "현재 로그인한 사용자의 정보를 조회합니다. 이름, 이메일, 소속 학과, 학번 등을 확인할 수 있습니다.")
    public String getMyInfo() {
        try {
            log.info("Tool called: getMyInfo()");
            MemberInfoResponse info = memberQuery.getMemberInfo();
            return objectMapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize MemberInfoResponse", e);
            return "사용자 정보 조회 중 오류가 발생했습니다: " + e.getMessage();
        } catch (Exception e) {
            log.error("Failed to get member info", e);
            return "사용자 정보 조회 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
