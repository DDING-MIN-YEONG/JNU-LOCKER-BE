package com.jnulocker.ai.application.tools;

import static com.jnulocker.member.domain.Role.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventPageable;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventManagerTools {

    private final EventQuery eventQuery;
    private final MemberQuery memberQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "[관리자 전용] 전체 사물함 신청 이벤트 목록을 조회합니다. 진행 중, 예정, 종료된 모든 이벤트를 검색할 수 있습니다.")
    @AiToolMethod
    public String searchEvents(
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page,
            @ToolParam(description = "페이지 크기 (기본값 10)", required = false) Integer size)
            throws JsonProcessingException {
        MemberInfoResponse currentUser = memberQuery.getMemberInfo();
        if (isNotManager(currentUser)) {
            return objectMapper.writeValueAsString(Map.of("error", "이 기능은 관리자만 사용할 수 있습니다."));
        }

        Pageable pageable = createPageable(page, size);
        EventCustomPage result = eventQuery.getAllEvents(pageable);
        return objectMapper.writeValueAsString(result);
    }

    private Pageable createPageable(Integer page, Integer size) {
        return new EventPageable(page, size, null, null).toPageable();
    }

    private boolean isNotManager(MemberInfoResponse currentUser) {
        return !MANAGER.equals(currentUser.role()) && !ADMIN.equals(currentUser.role());
    }
}
