package com.jnulocker.auth.application.port.in.response;

import com.jnulocker.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Page;

public record PendingManagerCustomPage(
        List<PendingManagerListItem> content, Long totalElements, Boolean last) {
    public static PendingManagerCustomPage from(Page<Member> pendingManagers) {
        return new PendingManagerCustomPage(
                pendingManagers.getContent().stream().map(PendingManagerListItem::from).toList(),
                pendingManagers.getTotalElements(),
                pendingManagers.isLast());
    }
}
