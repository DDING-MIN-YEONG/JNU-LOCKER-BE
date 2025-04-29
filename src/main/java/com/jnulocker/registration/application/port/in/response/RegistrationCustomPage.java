package com.jnulocker.registration.application.port.in.response;

import com.jnulocker.registration.domain.Registration;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

public record RegistrationCustomPage(
        @Schema(description = "신청 목록") List<RegistrationListItem> content,
        @Schema(description = "전체 신청 개수", example = "10") Long totalElements,
        @Schema(description = "마지막 페이지 여부", example = "false") Boolean last) {
    public static RegistrationCustomPage from(Page<Registration> registrations) {
        return new RegistrationCustomPage(
                registrations.getContent().stream().map(RegistrationListItem::from).toList(),
                registrations.getTotalElements(),
                registrations.isLast());
    }
}
