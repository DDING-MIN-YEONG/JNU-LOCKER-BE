package com.jnulocker.common.swagger.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@Schema(description = "페이지네이션을 위한 Swagger 모델입니다.", title = "SwaggerPageable")
public record CustomPageable(
        @Schema(description = "페이지 번호", example = "0")
                @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
                Integer page,
        @Schema(description = "페이지 크기", example = "10")
                @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
                Integer size,
        @Schema(description = "정렬 방향: asc(오름차순), desc(내림차순)", example = "asc")
                @Pattern(
                        regexp = "^(asc|ASC|desc|DESC)$",
                        message = "정렬 방향은 asc 또는 desc만 가능합니다. (대소문자 구분 없음)")
                String direction,
        @Schema(description = "정렬 기준: createdAt, eventSchedule.startAt 등", example = "createdAt")
                String sort) {

    public CustomPageable {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        if (direction == null) {
            direction = "asc";
        }
        if (sort == null) {
            sort = "createdAt";
        }
    }

    private static final Set<String> VALID_SORT_FIELDS =
            Set.of(
                    "id",
                    "title",
                    "publish",
                    "eventSchedule.startAt",
                    "eventSchedule.endAt",
                    "eventStatus",
                    "createdAt",
                    "updatedAt");

    @AssertTrue(message = "정렬 기준은 Event 엔티티의 속성 중 하나여야 합니다.")
    private boolean isValidSort() {
        return !sort.isBlank() && VALID_SORT_FIELDS.contains(sort);
    }

    public Pageable toPageable() {
        return PageRequest.of(page, size, Direction.fromString(direction), sort);
    }
}
