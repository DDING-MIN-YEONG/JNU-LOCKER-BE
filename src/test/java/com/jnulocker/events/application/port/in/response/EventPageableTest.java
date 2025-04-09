package com.jnulocker.events.application.port.in.response;

import static common.swagger.model.CustomPageableTestDataBuilder.pageableBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("이벤트 도메인 Pageable 검증 테스트")
class EventPageableTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_sort_값은_검증을_통과한다() {
        EventPageable pageable = pageableBuilder().withSort("createdAt").buildEventPageable();
        Set<ConstraintViolation<EventPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest(name = "정렬 기준: {0}")
    @MethodSource("provideValidSortFields")
    @DisplayName("Event 도메인의 유효한 sort 값은 검증을 통과한다")
    void 모든_유효한_sort_값은_검증을_통과한다(String sort) {
        EventPageable pageable = pageableBuilder().withSort(sort).buildEventPageable();
        Set<ConstraintViolation<EventPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    private static Stream<String> provideValidSortFields() {
        return EventPageable.VALID_SORT_FIELDS.stream();
    }

    @Test
    void 빈_sort_값은_검증에_실패한다() {
        EventPageable pageable = pageableBuilder().withSort("  ").buildEventPageable();
        Set<ConstraintViolation<EventPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo(EventPageable.VALID_SORT_MESSAGE);
    }

    @Test
    void 유효하지_않은_sort_값은_검증에_실패한다() {
        EventPageable pageable = pageableBuilder().withSort("invalidField").buildEventPageable();
        Set<ConstraintViolation<EventPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo(EventPageable.VALID_SORT_MESSAGE);
    }

    @Test
    void sort가_null이면_기본값으로_설정되어_검증을_통과한다() {
        EventPageable pageable = pageableBuilder().withSort(null).buildEventPageable();
        Set<ConstraintViolation<EventPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
        assertThat(pageable.getSort())
                .isEqualTo(EventPageable.DEFAULT_SORT); // EventPageable의 기본값 확인
    }
}
