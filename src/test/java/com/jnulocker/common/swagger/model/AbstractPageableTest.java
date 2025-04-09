package com.jnulocker.common.swagger.model;

import static common.swagger.model.CustomPageableTestDataBuilder.pageableBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("페이지네이션 요청 파라미터 검증 테스트")
class AbstractPageableTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        AbstractPageable pageable = pageableBuilder().buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    // 페이지 번호(page) 테스트
    @Test
    void 페이지_번호가_음수이면_검증에_실패한다() {
        AbstractPageable pageable = pageableBuilder().withPage(-1).buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("페이지 번호는 0 이상이어야 합니다.");
    }

    @Test
    void 페이지_번호가_null이면_기본값으로_설정되어_검증을_통과한다() {
        AbstractPageable pageable = pageableBuilder().withPage(null).buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    // 페이지 크기(size) 테스트
    @Test
    void 페이지_크기가_0이면_검증에_실패한다() {
        AbstractPageable pageable = pageableBuilder().withSize(0).buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("페이지 크기는 1 이상이어야 합니다.");
    }

    @Test
    void 페이지_크기가_음수이면_검증에_실패한다() {
        AbstractPageable pageable = pageableBuilder().withSize(-1).buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("페이지 크기는 1 이상이어야 합니다.");
    }

    @Test
    void 페이지_크기가_null이면_기본값으로_설정되어_검증을_통과한다() {
        AbstractPageable pageable = pageableBuilder().withSize(null).buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    // 정렬 방향(direction) 테스트
    @Test
    void 정렬_방향이_유효하지_않으면_검증에_실패한다() {
        AbstractPageable pageable = pageableBuilder().withDirection("up").buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("정렬 방향은 asc 또는 desc만 가능합니다. (대소문자 구분 없음)");
    }

    @Test
    void 정렬_방향이_null이면_기본값으로_설정되어_검증을_통과한다() {
        AbstractPageable pageable = pageableBuilder().withDirection(null).buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest(name = "정렬 방향: {0}")
    @ValueSource(strings = {"asc", "desc", "ASC", "DESC"})
    @DisplayName("정렬 방향이 asc, desc, ASC, DESC일 때 검증을 통과한다")
    void 정렬_방향이_유효하면_검증을_통과한다(String direction) {
        AbstractPageable pageable = pageableBuilder().withDirection(direction).buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).isEmpty();
    }

    // 복합 조건 테스트
    @Test
    void 여러_필드가_유효하지_않으면_모든_위반_메시지가_반환된다() {
        AbstractPageable pageable =
                pageableBuilder().withPage(-1).withSize(0).withDirection("up").buildTestPageable();
        Set<ConstraintViolation<AbstractPageable>> violations = validator.validate(pageable);
        assertThat(violations).hasSize(3);
        assertThat(violations)
                .extracting("message")
                .containsExactlyInAnyOrder(
                        "페이지 번호는 0 이상이어야 합니다.",
                        "페이지 크기는 1 이상이어야 합니다.",
                        "정렬 방향은 asc 또는 desc만 가능합니다. (대소문자 구분 없음)");
    }
}
