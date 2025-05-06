package com.jnulocker.events.application.port.in.response;

import com.jnulocker.events.domain.Event;
import java.time.LocalDateTime;

public record MyEventResponse(
        Long eventId,
        String title,
        String departmentNickname,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Integer availableLockerCount) {

    public static MyEventResponse of(Event event, Integer availableLockerCount) {
        return new MyEventResponse(
                event.getId(),
                event.getTitle(),
                event.getDepartment().getNickname(),
                event.getEventSchedule().getStartAt(),
                event.getEventSchedule().getEndAt(),
                availableLockerCount);
    }
}
