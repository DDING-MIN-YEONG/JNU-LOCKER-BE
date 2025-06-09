package com.jnulocker.events.application.port.in.request;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LockerValidationUtils {

    public static final int MAX_LOCKER_COUNT = 2000;

    public static boolean isTotalLockersWithinLimit(List<FloorInfo> floors) {
        int lockerCount =
                floors.stream()
                        .flatMap(floor -> floor.prefixes().stream())
                        .flatMap(prefix -> prefix.ranges().stream())
                        .filter(
                                range ->
                                        range.lockerStartNumber() != null
                                                && range.lockerEndNumber() != null)
                        .mapToInt(range -> range.lockerEndNumber() - range.lockerStartNumber() + 1)
                        .sum();
        return lockerCount <= MAX_LOCKER_COUNT;
    }
}
