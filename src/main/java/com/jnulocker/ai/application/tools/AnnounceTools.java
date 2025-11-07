package com.jnulocker.ai.application.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.announce.application.port.in.AnnounceQuery;
import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.AnnouncePageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnounceTools {

    private final AnnounceQuery announceQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "전체 공지사항 목록을 조회합니다. 최신 공지사항부터 시간 순으로 정렬되어 표시됩니다.")
    public String searchAnnouncements(
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page) {
        try {
            log.info("Tool called: searchAnnouncements(page={})", page);
            Pageable pageable = new AnnouncePageable(page, null, "desc", null).toPageable();
            AnnounceCustomPage result = announceQuery.getAnnounces(pageable);
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize AnnounceCustomPage", e);
            return "공지사항 목록 조회 중 오류가 발생했습니다: ";
        } catch (Exception e) {
            log.error("Failed to search announcements", e);
            return "공지사항 검색 중 오류가 발생했습니다: ";
        }
    }
}
