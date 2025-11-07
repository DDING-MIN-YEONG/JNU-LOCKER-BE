package com.jnulocker.ai.application.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventPageable;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventTools {

    private final EventQuery eventQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "사물함 신청 이벤트 목록을 검색합니다. 진행 중, 예정, 종료된 모든 이벤트를 조회할 수 있습니다.")
    @AiToolMethod
    public String searchEvents(
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page,
            @ToolParam(description = "페이지 크기 (기본값 10)", required = false) Integer size)
            throws JsonProcessingException {
        Pageable pageable = new EventPageable(page, size, null, null).toPageable();
        EventCustomPage result = eventQuery.getAllEvents(pageable);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(
            description =
                    "현재 로그인한 사용자가 참여(신청) 가능한 사물함 신청 이벤트 목록을 조회합니다. 사용자의 소속 학과에 해당하는 이벤트만 표시됩니다.")
    @AiToolMethod
    public String getMyEvents(
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page)
            throws JsonProcessingException {
        Pageable pageable = new EventPageable(page, null, null, null).toPageable();
        MyEventCustomPage result = eventQuery.getMyEvents(pageable);
        return objectMapper.writeValueAsString(result);
    }
}
