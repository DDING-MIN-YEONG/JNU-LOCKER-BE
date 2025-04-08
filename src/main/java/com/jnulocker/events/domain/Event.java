package com.jnulocker.events.domain;

import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.organization.domain.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Embedded private EventSchedule eventSchedule;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    private Boolean publish;

    public static Event create(
            String title,
            Department department,
            LocalDateTime startAt,
            LocalDateTime endAt,
            EventStatus eventStatus,
            Boolean publish) {
        return Event.builder()
                .title(title)
                .department(department)
                .eventSchedule(EventSchedule.of(startAt, endAt))
                .eventStatus(eventStatus)
                .publish(publish)
                .build();
    }

    public void openEvent() {
        eventStatus = EventStatus.OPEN;
    }

    public void closeEvent() {
        eventStatus = EventStatus.CLOSED;
    }
}
