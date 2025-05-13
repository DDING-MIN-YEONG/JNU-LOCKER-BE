package com.jnulocker.events.application.port.in.response;

import com.jnulocker.common.swagger.model.AbstractPageable;
import jakarta.validation.constraints.AssertTrue;
import java.util.Set;

public class EventPageable extends AbstractPageable {

    public static final String DEFAULT_SORT = "eventSchedule.startAt";

    public static final String VALID_SORT_MESSAGE =
            "정렬 기준은 다음 중 하나여야 합니다: [id, title, publish, eventSchedule.startAt, eventSchedule.endAt, eventStatus, createdAt, updatedAt].";

    // Event 도메인에 맞는 정렬 필드
    public static final Set<String> VALID_SORT_FIELDS =
            Set.of(
                    "id",
                    "title",
                    "publish",
                    "eventSchedule.startAt",
                    "eventSchedule.endAt",
                    "eventStatus",
                    "createdAt",
                    "updatedAt");

    public EventPageable(Integer page, Integer size, String direction, String sort) {
        super(page, size, direction, sort);
    }

    @Override
    @AssertTrue(message = VALID_SORT_MESSAGE)
    protected boolean isValidSort() {
        return !getSort().isBlank() && VALID_SORT_FIELDS.contains(getSort());
    }

    // Event 도메인에 맞는 기본 정렬 값
    @Override
    protected String getDefaultSort() {
        return DEFAULT_SORT;
    }
}
