package com.jnulocker.events.application.port.in.request;

import static events.application.port.in.request.CreateEventRequestTestDataBuilder.*;
import static events.application.port.in.request.FloorInfoTestDataBuilder.*;
import static jakarta.validation.Validation.*;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("이벤트 생성 요청 본문 검증 테스트")
class CreateEventRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        CreateEventRequest request = createEventRequestBuilder().build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 제목이_빈_문자열이면_검증에_실패한다() {
        CreateEventRequest request = createEventRequestBuilder().withTitle("").build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 제목은 필수입니다.");
    }

    @Test
    void 제목이_null이면_검증에_실패한다() {
        CreateEventRequest request = createEventRequestBuilder().withTitle(null).build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 제목은 필수입니다.");
    }

    @Test
    void departmentId가_null이면_검증에_실패한다() {
        CreateEventRequest request = createEventRequestBuilder().withDepartmentId(null).build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("주최 조직 department ID는 필수입니다.");
    }

    @Test
    void 시작_시간이_null이면_검증에_실패한다() {
        CreateEventRequest request = createEventRequestBuilder().withStartAt(null).build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 시작 시간은 필수입니다.");
    }

    @Test
    void 종료_시간이_null이면_검증에_실패한다() {
        CreateEventRequest request = createEventRequestBuilder().withEndAt(null).build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 종료 시간은 필수입니다.");
    }

    @Test
    void 종료_시간이_시작_시간_이전이면_검증에_실패한다() {
        CreateEventRequest request =
                createEventRequestBuilder()
                        .withStartAt(LocalDateTime.of(2025, 8, 1, 16, 0))
                        .withEndAt(LocalDateTime.of(2025, 8, 1, 15, 0))
                        .build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("이벤트 종료 시간은 시작 시간 이후여야 합니다.");
    }

    @Test
    void 참여_학과_목록이_비어있으면_검증에_실패한다() {
        CreateEventRequest request =
                createEventRequestBuilder()
                        .withParticipationDepartmentIds(Collections.emptyList())
                        .build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("참여 학과/학부는 최소 1개 이상이어야 합니다.");
    }

    @Test
    void 층_정보_목록이_비어있으면_검증에_실패한다() {
        CreateEventRequest request =
                createEventRequestBuilder().withFloors(Collections.emptyList()).build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("층 정보는 최소 1개 이상이어야 합니다.");
    }

    @Test
    void 층_번호가_null이면_검증에_실패한다() {
        CreateEventRequest request =
                createEventRequestBuilder()
                        .withFloors(List.of(floorInfoBuilder().withFloorNumber(null).build()))
                        .build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("층 번호는 필수입니다.");
    }

    @Test
    void 사물함_시작_번호가_null이면_검증에_실패한다() {
        CreateEventRequest request =
                createEventRequestBuilder()
                        .withFloors(List.of(floorInfoBuilder().withLockerStartNumber(null).build()))
                        .build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("사물함 시작 번호는 필수입니다.");
    }

    @Test
    void 사물함_종료_번호가_null이면_검증에_실패한다() {
        CreateEventRequest request =
                createEventRequestBuilder()
                        .withFloors(List.of(floorInfoBuilder().withLockerEndNumber(null).build()))
                        .build();

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("사물함 종료 번호는 필수입니다.");
    }
}
