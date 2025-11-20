package com.jnulocker.ai.application.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.announce.application.port.in.AnnounceQuery;
import com.jnulocker.announce.application.port.in.response.AnnouncePageable;
import com.jnulocker.announce.application.port.in.response.MyAnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.MyAnnounceDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnnounceTools {

    private final AnnounceQuery announceQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "내가 속한 학과의 공지사항 목록을 조회합니다. 최신 공지사항부터 시간 순으로 정렬되어 표시됩니다.")
    @AiToolMethod
    public String searchAnnouncements(
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page)
            throws JsonProcessingException {
        Pageable pageable = new AnnouncePageable(page, null, "desc", null).toPageable();
        MyAnnounceCustomPage result = announceQuery.getMyAnnounces(pageable);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(description = "내가 속한 학과의 특정 공지사항 상세 내용을 조회합니다. 제목, 내용, 작성자, 작성/수정 시간을 확인할 수 있습니다.")
    @AiToolMethod
    public String getAnnouncementDetail(@ToolParam(description = "공지사항 ID (숫자)") Long announceId)
            throws JsonProcessingException {
        MyAnnounceDetailResponse result = announceQuery.getMyAnnounce(announceId);
        return objectMapper.writeValueAsString(result);
    }
}
