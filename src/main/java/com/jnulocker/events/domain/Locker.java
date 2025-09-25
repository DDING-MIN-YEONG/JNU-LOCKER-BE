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
public class Locker extends BaseEntity implements Comparable<Locker> {

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

    @Override
    public int compareTo(Locker other) {
        String[] thisCodeParts = parseCode(this.code);
        String[] otherCodeParts = parseCode(other.code);

        String thisPrefix = thisCodeParts[0];
        String otherPrefix = otherCodeParts[0];
        int thisNumber = Integer.parseInt(thisCodeParts[1]);
        int otherNumber = Integer.parseInt(otherCodeParts[1]);

        // 1. 접두사 비교 (null인 경우 빈 문자열로 처리)
        String safeThisPrefix = thisPrefix != null ? thisPrefix : "";
        String safeOtherPrefix = otherPrefix != null ? otherPrefix : "";
        int prefixComparison = safeThisPrefix.compareTo(safeOtherPrefix);

        if (prefixComparison != 0) {
            return prefixComparison;
        }

        // 2. 번호 비교 (숫자로 비교)
        return Integer.compare(thisNumber, otherNumber);
    }

    private String[] parseCode(String code) {
        if (code.contains("-")) {
            // 접두사가 있는 경우: "A-001" -> ["A", "001"]
            String[] parts = code.split("-", 2);
            return new String[] {parts[0], parts[1]};
        } else {
            // 접두사가 없는 경우: "001" -> [null, "001"]
            return new String[] {null, code};
        }
    }
}
