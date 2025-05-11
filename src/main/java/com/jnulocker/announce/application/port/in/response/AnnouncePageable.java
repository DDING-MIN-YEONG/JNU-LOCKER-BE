package com.jnulocker.announce.application.port.in.response;

import com.jnulocker.common.swagger.model.AbstractPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import java.util.Set;

@Schema()
public class AnnouncePageable extends AbstractPageable {

    public static final String DEFAULT_SORT = "createdAt";

    public static final String VALID_SORT_MESSAGE =
            "정렬 기준은 다음 중 하나여야 합니다: [id, title, createdAt, updatedAt].";

    // Announce 도메인에 맞는 정렬 필드
    public static final Set<String> VALID_SORT_FIELDS =
            Set.of("id", "title", "createdAt", "updatedAt");

    public AnnouncePageable(Integer page, Integer size, String direction, String sort) {
        super(page, size, direction, sort);
    }

    @Override
    @AssertTrue(message = VALID_SORT_MESSAGE)
    protected boolean isValidSort() {
        return !getSort().isBlank() && VALID_SORT_FIELDS.contains(getSort());
    }

    // Announce 도메인에 맞는 기본 정렬 값
    @Override
    protected String getDefaultSort() {
        return DEFAULT_SORT;
    }
}
