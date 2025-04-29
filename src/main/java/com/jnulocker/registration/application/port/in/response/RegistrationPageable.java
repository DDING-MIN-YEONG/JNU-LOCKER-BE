package com.jnulocker.registration.application.port.in.response;

import com.jnulocker.common.swagger.model.AbstractPageable;
import jakarta.validation.constraints.AssertTrue;
import java.util.Set;

public class RegistrationPageable extends AbstractPageable {

    public static final String DEFAULT_SORT = "createdAt";

    public static final String VALID_SORT_MESSAGE =
            "정렬 기준은 다음 중 하나여야 합니다: [id, memberId, lockerId, createdAt, updatedAt].";

    public static final Set<String> VALID_SORT_FIELDS =
            Set.of("id", "memberId", "lockerId", "createdAt", "updatedAt");

    public RegistrationPageable(Integer page, Integer size, String direction, String sort) {
        super(page, size, direction, sort);
    }

    @Override
    @AssertTrue(message = VALID_SORT_MESSAGE)
    protected boolean isValidSort() {
        return !getSort().isBlank() && VALID_SORT_FIELDS.contains(getSort());
    }

    @Override
    protected String getDefaultSort() {
        return DEFAULT_SORT;
    }
}
