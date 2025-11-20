package com.jnulocker.ai.application.tools;

import static com.jnulocker.ai.application.tools.AiToolUtils.parseUUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.registration.application.port.in.RegistrationCommand;
import com.jnulocker.registration.application.port.in.RegistrationQuery;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import com.jnulocker.registration.exception.RegistrationNotFoundException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationTools {

    private final RegistrationQuery registrationQuery;
    private final RegistrationCommand registrationCommand;
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

    @Tool(description = "사물함 신청 이벤트에 참여하여 원하는 사물함을 신청합니다. 신청 가능 시간과 중복 신청 여부를 자동으로 검증합니다.")
    @AiToolMethod
    public String registerForLocker(
            @ToolParam(description = "이벤트 ID (UUID 형식)") String eventId,
            @ToolParam(description = "사물함 ID (UUID 형식)") String lockerId)
            throws JsonProcessingException {
        UUID eventUUID = parseUUID(eventId);
        UUID lockerUUID = parseUUID(lockerId);

        RegisterForEventRequest request = new RegisterForEventRequest(lockerUUID);
        registrationCommand.registerForEvent(eventUUID, request);

        return objectMapper.writeValueAsString(
                Map.of("success", true, "message", "사물함 신청이 완료되었습니다."));
    }

    @Tool(description = "신청한 사물함을 취소합니다. 신청 취소 가능 기간 내에만 실행할 수 있습니다.")
    @AiToolMethod
    public String cancelRegistration(@ToolParam(description = "이벤트 ID (UUID 형식)") String eventId)
            throws JsonProcessingException {
        UUID eventUUID = parseUUID(eventId);
        registrationCommand.cancelMyRegistration(eventUUID);

        return objectMapper.writeValueAsString(
                Map.of("success", true, "message", "사물함 신청이 취소되었습니다."));
    }
}
