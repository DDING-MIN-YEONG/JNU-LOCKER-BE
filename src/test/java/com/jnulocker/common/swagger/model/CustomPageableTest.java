package com.jnulocker.common.swagger.model;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CustomPageableTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_sort_값은_검증을_통과한다() {
        CustomPageable pageable = new CustomPageable(0, 10, "asc", "createdAt");
        Set<ConstraintViolation<CustomPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    @Test
    void 빈_sort_값은_검증에_실패한다() {
        CustomPageable pageable = new CustomPageable(0, 10, "asc", "  ");
        Set<ConstraintViolation<CustomPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("정렬 기준은 Event 엔티티의 속성 중 하나여야 합니다.");
    }

    @Test
    void 유효하지_않은_sort_값은_검증에_실패한다() {
        CustomPageable pageable = new CustomPageable(0, 10, "asc", "invalidField");
        Set<ConstraintViolation<CustomPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("정렬 기준은 Event 엔티티의 속성 중 하나여야 합니다.");
    }
}
