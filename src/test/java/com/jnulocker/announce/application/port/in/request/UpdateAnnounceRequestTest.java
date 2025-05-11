package com.jnulocker.announce.application.port.in.request;

import static announce.application.port.in.request.UpdateAnnounceRequestTestDataBuilder.updateAnnounceRequestBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("공지사항 수정 요청 본문 검증 테스트")
class UpdateAnnounceRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 제목이_빈_문자열이면_검증에_실패한다() {
        UpdateAnnounceRequest request = updateAnnounceRequestBuilder().withTitle("").build();

        Set<ConstraintViolation<UpdateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 제목은 필수입니다.");
    }

    @Test
    void 제목이_null이면_검증에_실패한다() {
        UpdateAnnounceRequest request = updateAnnounceRequestBuilder().withTitle(null).build();

        Set<ConstraintViolation<UpdateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 제목은 필수입니다.");
    }

    @Test
    void 제목이_50자를_초과하면_검증에_실패한다() {
        String longTitle = "a".repeat(51);
        UpdateAnnounceRequest request = updateAnnounceRequestBuilder().withTitle(longTitle).build();

        Set<ConstraintViolation<UpdateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공지사항 제목은 50자를 초과할 수 없습니다.");
    }

    @Test
    void 내용이_빈_문자열이면_검증에_실패한다() {
        UpdateAnnounceRequest request = updateAnnounceRequestBuilder().withContent("").build();

        Set<ConstraintViolation<UpdateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 내용은 필수입니다.");
    }

    @Test
    void 내용이_null이면_검증에_실패한다() {
        UpdateAnnounceRequest request = updateAnnounceRequestBuilder().withContent(null).build();

        Set<ConstraintViolation<UpdateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 내용은 필수입니다.");
    }

    @Test
    void 내용이_1500자를_초과하면_검증에_실패한다() {
        String longContent = "a".repeat(1501);
        UpdateAnnounceRequest request =
                updateAnnounceRequestBuilder().withContent(longContent).build();

        Set<ConstraintViolation<UpdateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공지사항 내용은 1500자를 초과할 수 없습니다.");
    }

    @Test
    void 참여_학과_목록이_비어있으면_검증에_실패한다() {
        UpdateAnnounceRequest request =
                updateAnnounceRequestBuilder()
                        .withParticipationDepartmentIds(Collections.emptyList())
                        .build();

        Set<ConstraintViolation<UpdateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("참여 학과/학부는 최소 1개 이상이어야 합니다.");
    }
}
