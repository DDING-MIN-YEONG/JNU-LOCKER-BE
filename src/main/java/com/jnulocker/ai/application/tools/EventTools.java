package com.jnulocker.ai.application.tools;

import static com.jnulocker.ai.application.tools.AiToolUtils.parseUUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.events.application.port.in.EventQuery;
import com.jnulocker.events.application.port.in.response.AvailableLockersResponse;
import com.jnulocker.events.application.port.in.response.EventPageable;
import com.jnulocker.events.application.port.in.response.EventResponse;
import com.jnulocker.events.application.port.in.response.LockerSummaryResponse;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.application.port.in.response.PagedLockersResponse;
import java.util.UUID;
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

    @Tool(
            description =
                    "현재 로그인한 사용자가 참여(신청) 가능한 사물함 신청 이벤트 목록을 조회합니다. 사용자의 소속 학과에 해당하는 이벤트만 표시됩니다.")
    @AiToolMethod
    public String getMyEvents(
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page,
            @ToolParam(description = "페이지 크기 (기본값 10)", required = false) Integer size)
            throws JsonProcessingException {
        Pageable pageable = createPageable(page, size);
        MyEventCustomPage result = eventQuery.getMyEvents(pageable);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(description = "특정 사물함 신청 이벤트의 상세 정보를 조회합니다. 이벤트 제목, 시작/종료 시간, 참여 학과, 층 정보 등을 확인할 수 있습니다.")
    @AiToolMethod
    public String getEventDetails(@ToolParam(description = "이벤트 ID (UUID 형식)") String eventId)
            throws JsonProcessingException {
        UUID uuid = parseUUID(eventId);
        EventResponse result = eventQuery.getEvent(uuid);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(
            description =
                    "특정 이벤트의 사물함 가용 현황 요약 정보를 조회합니다. 전체 사물함 개수, 사용 가능한 사물함 개수, 층별 집계 정보를 제공합니다.")
    @AiToolMethod
    public String getLockerSummary(@ToolParam(description = "이벤트 ID (UUID 형식)") String eventId)
            throws JsonProcessingException {
        UUID uuid = parseUUID(eventId);
        LockerSummaryResponse result = eventQuery.getLockerSummary(uuid);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(description = "특정 이벤트의 특정 층에 있는 사물함 목록을 조회합니다. 페이지 단위로 조회 가능")
    @AiToolMethod
    public String getLockersByFloor(
            @ToolParam(description = "이벤트 ID (UUID 형식)") String eventId,
            @ToolParam(description = "조회할 층 번호 (예: 1, 2, 3)") Integer floorNumber,
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page,
            @ToolParam(description = "페이지 크기 (기본값 10)", required = false) Integer size)
            throws JsonProcessingException {
        UUID uuid = parseUUID(eventId);
        Pageable pageable = createPageable(page, size);

        PagedLockersResponse result = eventQuery.getLockersByFloor(uuid, floorNumber, pageable);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(description = "특정 이벤트에서 현재 신청 가능한 사물함 목록만 조회합니다. 페이지 단위로 조회할 수 있습니다.")
    @AiToolMethod
    public String getAvailableLockers(
            @ToolParam(description = "이벤트 ID (UUID 형식)") String eventId,
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page,
            @ToolParam(description = "페이지 크기 (기본값 10)", required = false) Integer size)
            throws JsonProcessingException {
        UUID uuid = parseUUID(eventId);
        Pageable pageable = createPageable(page, size);

        AvailableLockersResponse result = eventQuery.getAvailableLockers(uuid, pageable);
        return objectMapper.writeValueAsString(result);
    }

    private Pageable createPageable(Integer page, Integer size) {
        return new EventPageable(page, size, null, null).toPageable();
    }
}
