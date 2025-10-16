package com.jnulocker.ai.application.port.in.response;

import com.jnulocker.common.swagger.model.AbstractPageable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import java.util.Set;

@Schema()
public class DocumentPageable extends AbstractPageable {

    public static final String DEFAULT_SORT = "createdAt";

    public static final String VALID_SORT_MESSAGE =
            "정렬 기준은 다음 중 하나여야 합니다: [id, fileName, fileSize, chunkCount, createdAt, updatedAt].";

    // AiDocument 도메인에 맞는 정렬 필드
    public static final Set<String> VALID_SORT_FIELDS =
            Set.of("id", "fileName", "fileSize", "chunkCount", "createdAt", "updatedAt");

    public DocumentPageable(Integer page, Integer size, String direction, String sort) {
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
