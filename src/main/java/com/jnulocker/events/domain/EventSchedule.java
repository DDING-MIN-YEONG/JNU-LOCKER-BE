package com.jnulocker.events.domain;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class EventSchedule {

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static EventSchedule of(LocalDateTime startAt, LocalDateTime endAt) {
        return EventSchedule.builder().startAt(startAt).endAt(endAt).build();
    }
}
