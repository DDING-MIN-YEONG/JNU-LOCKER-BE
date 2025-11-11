package com.jnulocker.ai.application.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnulocker.organization.application.port.in.DepartmentQuery;
import com.jnulocker.organization.application.port.in.OrganizationQuery;
import com.jnulocker.organization.application.port.in.response.DepartmentResponse;
import com.jnulocker.organization.application.port.in.response.OrganizationResponse;
import com.jnulocker.organization.domain.OrganizationType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationTools {

    private final OrganizationQuery organizationQuery;
    private final DepartmentQuery departmentQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "전남대학교의 조직 목록을 조회합니다. COUNCIL(단과대학), COMMITTEE(위원회) 타입으로 구분하여 조회할 수 있습니다.")
    @AiToolMethod
    public String searchOrganizations(
            @ToolParam(description = "조직 타입: COUNCIL 또는 COMMITTEE")
                    OrganizationType organizationType)
            throws JsonProcessingException {
        List<OrganizationResponse> result = organizationQuery.getOrganizations(organizationType);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(description = "특정 조직에 속한 학과/학부 목록을 조회합니다.")
    @AiToolMethod
    public String searchDepartments(@ToolParam(description = "조직 ID (숫자)") Long organizationId)
            throws JsonProcessingException {
        List<DepartmentResponse> result =
                departmentQuery.getDepartmentsByOrganizationId(organizationId);
        return objectMapper.writeValueAsString(result);
    }
}
