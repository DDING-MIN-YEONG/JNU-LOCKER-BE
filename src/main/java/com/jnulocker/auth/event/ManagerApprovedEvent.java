package com.jnulocker.auth.event;

import com.jnulocker.common.event.DomainEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ManagerApprovedEvent extends DomainEvent {
    private final String email;
    private final String department;

    public static ManagerApprovedEvent of(String email, String department) {
        return new ManagerApprovedEvent(email, department);
    }
}
