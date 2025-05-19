package com.jnulocker.events.domain;

import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.events.exception.EventNotOpenException;
import com.jnulocker.events.exception.EventNotPublishedException;
import com.jnulocker.events.exception.OnlyManagerCanDeleteEventException;
import com.jnulocker.events.exception.OnlyManagerCanUpdateEventException;
import com.jnulocker.events.exception.OnlyParticipationDepartmentCanRegisterException;
import com.jnulocker.events.exception.OnlyReadyEventCanBeUpdatedException;
import com.jnulocker.events.exception.OpenEventCannotBeDeletedException;
import com.jnulocker.organization.domain.Department;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    private UUID id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<EventParticipation> eventParticipations = new ArrayList<>();

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

    public void validateRegistration(Department department) {
        validateParticipationDepartmentRegistration(department);
        validatePublishStatus();
        validateEventStatus();
    }

    private void validateParticipationDepartmentRegistration(Department department) {
        if (!isParticipationDepartment(department)) {
            throw OnlyParticipationDepartmentCanRegisterException.EXCEPTION;
        }
    }

    public boolean isParticipationDepartment(Department department) {
        return eventParticipations.stream()
                .anyMatch(
                        eventParticipation ->
                                eventParticipation.getDepartment().equals(department));
    }

    public void validatePublishStatus() {
        if (Boolean.FALSE.equals(publish)) {
            throw EventNotPublishedException.EXCEPTION;
        }
    }

    private void validateEventStatus() {
        if (eventStatus != EventStatus.OPEN) {
            throw EventNotOpenException.EXCEPTION;
        }
    }

    public void validateDeletable(Department department) {
        if (!isSameDepartment(department)) {
            throw OnlyManagerCanDeleteEventException.EXCEPTION;
        }

        if (eventStatus == EventStatus.OPEN) {
            throw OpenEventCannotBeDeletedException.EXCEPTION;
        }
    }

    public boolean isSameDepartment(Department department) {
        return this.department.equals(department);
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

    public void updateInfo(String title, LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.eventSchedule = EventSchedule.of(startAt, endAt);
    }

    public void validateUpdatable(Department department) {
        if (!isSameDepartment(department)) {
            throw OnlyManagerCanUpdateEventException.EXCEPTION;
        }

        if (eventStatus != EventStatus.READY) {
            throw OnlyReadyEventCanBeUpdatedException.EXCEPTION;
        }
    }
}
