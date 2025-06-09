package com.jnulocker.events.infrastructure.mapper;

import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.request.LockerRange;
import com.jnulocker.events.application.port.in.request.PrefixInfo;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class LockerMapper {

    public static List<FloorInfo> toFloorInfos(List<FloorWithLockersResponse> floorWithLockers) {
        return floorWithLockers.stream().map(LockerMapper::toFloorInfo).toList();
    }

    private static FloorInfo toFloorInfo(FloorWithLockersResponse floorWithLockers) {
        // 사물함 코드에서 접두사와 번호를 추출하여 접두사별로 번호를 그룹화
        Map<String, List<Integer>> numbersByPrefix = new HashMap<>();
        for (LockerResponse locker : floorWithLockers.lockers()) {
            LockerCodeInfo codeInfo = extractLockerCodeInfo(locker.code());
            numbersByPrefix
                    .computeIfAbsent(codeInfo.prefix(), k -> new ArrayList<>())
                    .add(codeInfo.number());
        }

        // 접두사별로 번호를 정렬하고 범위로 변환
        List<PrefixInfo> prefixes = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : numbersByPrefix.entrySet()) {
            Collections.sort(entry.getValue());
            List<LockerRange> ranges = toLockerRanges(entry.getValue());
            prefixes.add(new PrefixInfo(entry.getKey(), ranges));
        }

        return new FloorInfo(floorWithLockers.floorNumber(), prefixes);
    }

    private static List<LockerRange> toLockerRanges(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return List.of();
        }

        List<LockerRange> ranges = new ArrayList<>();
        int start = numbers.getFirst();
        int prev = start;
        for (int i = 1; i <= numbers.size(); i++) {
            if (i == numbers.size() || numbers.get(i) > prev + 1) {
                ranges.add(new LockerRange(start, prev));
                if (i < numbers.size()) {
                    start = numbers.get(i);
                    prev = start;
                }
            } else {
                prev = numbers.get(i);
            }
        }
        return ranges;
    }

    private static LockerCodeInfo extractLockerCodeInfo(String code) {
        int hyphenIndex = code.lastIndexOf('-');
        String prefix = null;
        String numberPart = code;
        if (hyphenIndex >= 0) {
            prefix = code.substring(0, hyphenIndex);
            numberPart = code.substring(hyphenIndex + 1);
        }

        try {
            int number = Integer.parseInt(numberPart);
            return new LockerCodeInfo(prefix, number);
        } catch (NumberFormatException e) {
            log.warn("유효하지 않은 사물함 코드 형식: {}", code);
            return new LockerCodeInfo(code, 0);
        }
    }
}
