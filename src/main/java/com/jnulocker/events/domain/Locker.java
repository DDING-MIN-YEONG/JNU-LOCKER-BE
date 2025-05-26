package com.jnulocker.events.domain;

import com.jnulocker.common.persistence.BaseEntity;
import com.jnulocker.events.exception.LockerUnavailableException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Locker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "locker_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    private String code;

    private Boolean available;

    public static Locker create(Floor floor, String code, Boolean available) {
        return Locker.builder().floor(floor).code(code).available(available).build();
    }

    public void validateRegistration() {
        checkAvailability();
    }

    public void markAsUnavailable() {
        checkAvailability();
        this.available = false;
    }

    public void markAsAvailable() {
        this.available = true;
    }

    private void checkAvailability() {
        if (Boolean.FALSE.equals(available)) {
            throw LockerUnavailableException.EXCEPTION;
        }
    }
}
