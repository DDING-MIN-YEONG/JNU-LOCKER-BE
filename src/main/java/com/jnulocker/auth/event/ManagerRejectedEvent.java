package com.jnulocker.auth.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ManagerRejectedEvent {
    private final String email;
    private final String department;

    public static ManagerRejectedEvent of(String email, String department) {
        return new ManagerRejectedEvent(email, department);
    }
}
