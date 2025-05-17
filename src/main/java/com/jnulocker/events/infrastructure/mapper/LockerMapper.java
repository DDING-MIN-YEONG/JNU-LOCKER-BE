package com.jnulocker.events.infrastructure.mapper;

import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.request.LockerRange;
import com.jnulocker.events.application.port.in.request.PrefixInfo;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/** 사물함 정보를 다양한 형태로 변환하는 매퍼 클래스입니다. 주로 사물함 데이터를 API 요청/응답 형식으로 변환하는 기능을 담당합니다. */
@UtilityClass
public class LockerMapper {

    /**
     * 사물함 코드 패턴: prefix-number 또는 number 예: "A-123", "123", "B-456", "789" </br> 정규표현식 설명 -
     * (?:([A-Za-z]+)-)? : 선택적 접두사 (영문자)와 하이픈 </br> - ([A-Za-zㄱ-ㅣ가-힣]+) : 접두사 (영문자) </br> - - : 하이픈
     * </br> - ?:(...): 캡쳐하지 않고 그룹화 </br> - ...?: 0회 또는 1회 반복 </br> - 위 내용 해석: 문자와 하이픈을 포함한 접두사가 있거나
     * 없을 수 있고, 있다면 하이픈을 포함하지 않고 문자만 캡쳐 </br> - (\\d+) : 필수 숫자 부분
     */
    private static final Pattern LOCKER_CODE_PATTERN =
            Pattern.compile("^(?:([A-Za-zㄱ-ㅣ가-힣]+)-)?(\\d+)$");

    /**
     * FloorWithLockersResponse 목록을 FloorInfo 목록으로 변환합니다. 이 메소드는 사물함 코드를 분석하여 prefix, 숫자 범위를 추출하고
     * 그룹화합니다.
     */
    public static List<FloorInfo> toFloorInfos(List<FloorWithLockersResponse> floorWithLockers) {
        return floorWithLockers.stream().map(LockerMapper::toFloorInfo).toList();
    }

    /** 단일 FloorWithLockersResponse를 FloorInfo로 변환합니다. */
    private static FloorInfo toFloorInfo(FloorWithLockersResponse floorWithLockers) {
        // 같은 prefix를 가진 사물함을 그룹화
        Map<String, List<LockerResponse>> lockersByPrefix =
                groupLockersByPrefix(floorWithLockers.lockers());

        // 각 prefix 그룹을 PrefixInfo로 변환
        List<PrefixInfo> prefixes =
                lockersByPrefix.entrySet().stream()
                        .map(entry -> toPrefixInfo(entry.getKey(), entry.getValue()))
                        .toList();

        return new FloorInfo(floorWithLockers.floorNumber(), prefixes);
    }

    /** 사물함 목록을 prefix별로 그룹화합니다. */
    private static Map<String, List<LockerResponse>> groupLockersByPrefix(
            List<LockerResponse> lockers) {
        Map<String, List<LockerResponse>> result = new HashMap<>();

        for (LockerResponse locker : lockers) {
            // 사물함 코드에서 prefix와 number 추출
            LockerCodeInfo codeInfo = extractLockerCodeInfo(locker.code());

            // prefix별로 그룹화
            String prefix = codeInfo.prefix() != null ? codeInfo.prefix() : "";
            result.computeIfAbsent(prefix, k -> new ArrayList<>()).add(locker);
        }

        return result;
    }

    /** prefix와 사물함 목록을 PrefixInfo로 변환합니다. */
    private static PrefixInfo toPrefixInfo(String prefix, List<LockerResponse> lockers) {
        // 각 사물함 코드의 숫자 부분 추출
        List<Integer> numbers =
                lockers.stream()
                        .map(locker -> extractLockerCodeInfo(locker.code()).number())
                        .sorted()
                        .toList();

        // 연속된 숫자 범위를 LockerRange로 변환
        List<LockerRange> ranges = toLockerRanges(numbers);

        // prefix가 빈 문자열이면 null로 설정 (사물함 코드가 숫자만 있는 경우)
        String actualPrefix = prefix.isEmpty() ? null : prefix;

        return new PrefixInfo(actualPrefix, ranges);
    }

    /** 정렬된 숫자 목록을 연속된 범위로 그룹화하여 LockerRange 목록으로 변환합니다. */
    private static List<LockerRange> toLockerRanges(List<Integer> sortedNumbers) {
        if (sortedNumbers.isEmpty()) {
            return List.of();
        }

        List<LockerRange> ranges = new ArrayList<>();
        int start = sortedNumbers.getFirst();
        int prev = start;

        for (int i = 1; i < sortedNumbers.size(); i++) {
            int current = sortedNumbers.get(i);

            // 연속되지 않은 숫자를 만나면 새로운 범위 시작
            if (current > prev + 1) {
                ranges.add(new LockerRange(start, prev));
                start = current;
            }

            prev = current;
        }

        // 마지막 범위 추가
        ranges.add(new LockerRange(start, prev));

        return ranges;
    }

    /** 사물함 코드에서 prefix와 숫자 부분을 추출합니다. */
    private static LockerCodeInfo extractLockerCodeInfo(String code) {
        Matcher matcher = LOCKER_CODE_PATTERN.matcher(code);

        if (matcher.matches()) {
            String prefix = matcher.group(1); // prefix (optional)
            int number = Integer.parseInt(matcher.group(2)); // number
            return new LockerCodeInfo(prefix, number);
        }

        // 패턴이 일치하지 않으면 코드 전체를 숫자로 파싱 시도
        try {
            return new LockerCodeInfo(null, Integer.parseInt(code));
        } catch (NumberFormatException e) {
            // 숫자로 파싱할 수 없으면 prefix가 있는 것으로 간주하고 처리
            return new LockerCodeInfo(code, 0);
        }
    }
}
