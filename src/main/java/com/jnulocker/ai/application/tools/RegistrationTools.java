package com.jnulocker.ai.application.tools;

import static com.jnulocker.ai.application.tools.AiToolUtils.parseUUID;
import static com.jnulocker.member.domain.Role.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.member.application.port.in.MemberQuery;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import com.jnulocker.registration.application.port.in.RegistrationQuery;
import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationPageable;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import com.jnulocker.registration.exception.RegistrationNotFoundException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationTools {

    private final RegistrationQuery registrationQuery;
    private final MemberQuery memberQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "특정 이벤트에 대한 나의 사물함 신청 현황을 조회합니다. 신청한 사물함 번호, 위치(층), 신청 ID를 확인할 수 있습니다.")
    @AiToolMethod
    public String checkMyRegistration(@ToolParam(description = "이벤트 ID (UUID 형식)") String eventId)
            throws JsonProcessingException {
        try {
            UUID uuid = parseUUID(eventId);
            RegistrationResponse result = registrationQuery.getMyRegistration(uuid);
            return objectMapper.writeValueAsString(result);
        } catch (RegistrationNotFoundException e) {
            return objectMapper.writeValueAsString(Map.of("message", "해당 이벤트에 대한 신청 내역이 없습니다."));
        }
    }

    @Tool(
            description =
                    "[관리자 전용] 특정 이벤트의 전체 사물함 신청 현황을 조회합니다. 신청자 정보, 신청 시간, 사물함 배정 상태 등을 확인할 수 있습니다.")
    @AiToolMethod
    public String getRegistrationList(
            @ToolParam(description = "이벤트 ID (UUID 형식)") String eventId,
            @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page)
            throws JsonProcessingException {
        MemberInfoResponse currentUser = memberQuery.getMemberInfo();

        if (isNotManager(currentUser)) {
            return objectMapper.writeValueAsString(Map.of("error", "이 기능은 관리자만 사용할 수 있습니다."));
        }

        UUID eventUUID = parseUUID(eventId);
        Pageable pageable = new RegistrationPageable(page, null, "desc", null).toPageable();
        RegistrationCustomPage result = registrationQuery.getRegistrations(eventUUID, pageable);
        return objectMapper.writeValueAsString(result);
    }

    private boolean isNotManager(MemberInfoResponse currentUser) {
        return !MANAGER.equals(currentUser.role()) && !ADMIN.equals(currentUser.role());
    }
}
