package com.jnulocker.announce.application.port.in.request;

import static announce.application.CreateAnnounceReqeustTestDataBuilder.createAnnounceReqeustBuilder;
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

@DisplayName("공지사항 생성 요청 본문 검증 테스트")
public class CreateAnnounceRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        CreateAnnounceRequest request = createAnnounceReqeustBuilder().build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 제목이_빈_문자열이면_검증에_실패한다() {
        CreateAnnounceRequest request = createAnnounceReqeustBuilder().withTitle("").build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 제목은 필수입니다.");
    }

    @Test
    void 제목이_null이면_검증에_실패한다() {
        CreateAnnounceRequest request = createAnnounceReqeustBuilder().withTitle(null).build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 제목은 필수입니다.");
    }

    @Test
    void 제목이_50자를_초과하면_검증에_실패한다() {
        String longTitle = "a".repeat(51);
        CreateAnnounceRequest request = createAnnounceReqeustBuilder().withTitle(longTitle).build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공지사항 제목은 50자를 초과할 수 없습니다.");
    }

    @Test
    void 내용이_빈_문자열이면_검증에_실패한다() {
        CreateAnnounceRequest request = createAnnounceReqeustBuilder().withContent("").build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 내용은 필수입니다.");
    }

    @Test
    void 내용이_null이면_검증에_실패한다() {
        CreateAnnounceRequest request = createAnnounceReqeustBuilder().withContent(null).build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("공지사항 내용은 필수입니다.");
    }

    @Test
    void 내용이_1500자를_초과하면_검증에_실패한다() {
        String longContent = "a".repeat(1501);
        CreateAnnounceRequest request =
                createAnnounceReqeustBuilder().withContent(longContent).build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("공지사항 내용은 1500자를 초과할 수 없습니다.");
    }

    @Test
    void 참여_학과_목록이_비어있으면_검증에_실패한다() {
        CreateAnnounceRequest request =
                createAnnounceReqeustBuilder()
                        .withParticipationDepartmentIds(Collections.emptyList())
                        .build();

        Set<ConstraintViolation<CreateAnnounceRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("참여 학과/학부는 최소 1개 이상이어야 합니다.");
    }
}
