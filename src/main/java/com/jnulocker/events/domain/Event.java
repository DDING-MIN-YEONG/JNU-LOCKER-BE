package com.jnulocker.events.domain;

import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.events.exception.EventNotOpenException;
import com.jnulocker.events.exception.OnlyManagerCanDeleteException;
import com.jnulocker.events.exception.OpenEventCannotBeDeletedException;
import com.jnulocker.member.domain.Role;
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
            String title, Department department, LocalDateTime startAt, LocalDateTime endAt) {
        return Event.builder()
                .title(title)
                .department(department)
                .eventSchedule(EventSchedule.of(startAt, endAt))
                .eventStatus(EventStatus.READY)
                .publish(false)
                .build();
    }

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

    public void validateRegistration(Role role) {
        validatePublishStatus(role);
        validateEventStatus();
    }

    private void validatePublishStatus(Role role) {
        if (Boolean.FALSE.equals(publish) && role != Role.MANAGER) {
            throw EventNotOpenException.EXCEPTION;
        }
    }

    private void validateEventStatus() {
        if (eventStatus != EventStatus.OPEN) {
            throw EventNotOpenException.EXCEPTION;
        }
    }

    public void validateDeletable(Department department) {
        if (!this.department.equals(department)) {
            throw OnlyManagerCanDeleteException.EXCEPTION;
        }

        if (eventStatus == EventStatus.OPEN) {
            throw OpenEventCannotBeDeletedException.EXCEPTION;
        }
    }

    public void openEvent() {
        eventStatus = EventStatus.OPEN;
    }

    public void closeEvent() {
        eventStatus = EventStatus.CLOSED;
    }

    public void updatePublishStatus(Boolean publish) {
        this.publish = publish;
    }
}
