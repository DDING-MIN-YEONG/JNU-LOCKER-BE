package com.jnulocker.common.swagger.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@Getter
@Schema(description = "페이지네이션을 위한 Swagger 모델입니다.", title = "SwaggerPageable")
public abstract class AbstractPageable {
    @Schema(description = "페이지 번호", example = "0")
    @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
    private final Integer page;

    @Schema(description = "페이지 크기", example = "10")
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    private final Integer size;

    @Schema(description = "정렬 방향: asc(오름차순), desc(내림차순)", example = "asc")
    @Pattern(regexp = "^(asc|ASC|desc|DESC)$", message = "정렬 방향은 asc 또는 desc만 가능합니다. (대소문자 구분 없음)")
    private final String direction;

    @Schema(description = "정렬 기준", example = "createdAt")
    private final String sort;

    protected AbstractPageable(Integer page, Integer size, String direction, String sort) {
        this.page = (page != null) ? page : 0;
        this.size = (size != null) ? size : 10;
        this.direction = (direction != null) ? direction : "asc";
        this.sort = (sort != null) ? sort : getDefaultSort();
    }

    // 기본 정렬 값 제공 (필요 시 자식 클래스에서 오버라이드)
    protected String getDefaultSort() {
        return "createdAt";
    }

    protected abstract boolean isValidSort();

    public Pageable toPageable() {
        return PageRequest.of(page, size, Direction.fromString(direction), sort);
    }
}
