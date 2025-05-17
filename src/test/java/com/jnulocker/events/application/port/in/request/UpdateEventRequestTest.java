package com.jnulocker.events.application.port.in.request;

import static events.application.port.in.request.FloorInfoTestDataBuilder.*;
import static events.application.port.in.request.UpdateEventRequestTestDataBuilder.*;
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

@DisplayName("이벤트 수정 요청 본문 검증 테스트")
class UpdateEventRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        UpdateEventRequest request = updateEventRequestBuilder().build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 제목이_빈_문자열이면_검증에_실패한다() {
        UpdateEventRequest request = updateEventRequestBuilder().withTitle("").build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 제목은 필수입니다.");
    }

    @Test
    void 제목이_null이면_검증에_실패한다() {
        UpdateEventRequest request = updateEventRequestBuilder().withTitle(null).build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 제목은 필수입니다.");
    }

    @Test
    void 시작_시간이_null이면_검증에_실패한다() {
        UpdateEventRequest request = updateEventRequestBuilder().withStartAt(null).build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 시작 시간은 필수입니다.");
    }

    @Test
    void 종료_시간이_null이면_검증에_실패한다() {
        UpdateEventRequest request = updateEventRequestBuilder().withEndAt(null).build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이벤트 종료 시간은 필수입니다.");
    }

    @Test
    void 종료_시간이_시작_시간_이전이면_검증에_실패한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder()
                        .withStartAt(LocalDateTime.of(2025, 8, 2, 16, 0))
                        .withEndAt(LocalDateTime.of(2025, 8, 2, 15, 0))
                        .build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("이벤트 종료 시간은 시작 시간 이후여야 합니다.");
    }

    @Test
    void 참여_학과_목록이_비어있으면_검증에_실패한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder()
                        .withParticipationDepartmentIds(Collections.emptyList())
                        .build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("참여 학과/학부는 최소 1개 이상이어야 합니다.");
    }

    @Test
    void 층_정보_목록이_비어있으면_검증에_실패한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder().withFloors(Collections.emptyList()).build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("층 정보는 최소 1개 이상이어야 합니다.");
    }

    @Test
    void 층_번호가_null이면_검증에_실패한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder()
                        .withFloors(List.of(floorInfoBuilder().withFloorNumber(null).build()))
                        .build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("층 번호는 필수입니다.");
    }

    @Test
    void 사물함_시작_번호가_null이면_검증에_실패한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder()
                        .withFloors(
                                List.of(
                                        floorInfoBuilder()
                                                .withPrefixes(
                                                        List.of(
                                                                new PrefixInfo(
                                                                        "A",
                                                                        List.of(
                                                                                new LockerRange(
                                                                                        null,
                                                                                        20)))))
                                                .build()))
                        .build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("사물함 시작 번호는 필수입니다.");
    }

    @Test
    void 사물함_종료_번호가_null이면_검증에_실패한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder()
                        .withFloors(
                                List.of(
                                        floorInfoBuilder()
                                                .withPrefixes(
                                                        List.of(
                                                                new PrefixInfo(
                                                                        "A",
                                                                        List.of(
                                                                                new LockerRange(
                                                                                        1, null)))))
                                                .build()))
                        .build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("사물함 종료 번호는 필수입니다.");
    }

    @Test
    void 사물함_종료_번호가_시작_번호보다_작으면_검증에_실패한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder()
                        .withFloors(
                                List.of(
                                        floorInfoBuilder()
                                                .withPrefixes(
                                                        List.of(
                                                                new PrefixInfo(
                                                                        "A",
                                                                        List.of(
                                                                                new LockerRange(
                                                                                        10,
                                                                                        8) // 종료 번호
                                                                                // < 시작
                                                                                // 번호
                                                                                ))))
                                                .build()))
                        .build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("사물함 종료 번호는 시작 번호보다 크거나 같아야 합니다.");
    }

    @Test
    void 사물함_범위가_단일_번호여도_검증에_성공한다() {
        UpdateEventRequest request =
                updateEventRequestBuilder()
                        .withFloors(
                                List.of(
                                        floorInfoBuilder()
                                                .withPrefix("A", 5, 5) // 단일 사물함
                                                .build()))
                        .build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 시작_종료_시간이_같아도_검증에_성공한다() {
        LocalDateTime sameTime = LocalDateTime.of(2025, 8, 2, 15, 0);
        UpdateEventRequest request =
                updateEventRequestBuilder().withStartAt(sameTime).withEndAt(sameTime).build();

        Set<ConstraintViolation<UpdateEventRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
