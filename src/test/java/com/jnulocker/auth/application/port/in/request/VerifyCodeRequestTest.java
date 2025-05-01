package com.jnulocker.auth.application.port.in.request;

import static auth.application.port.in.request.VerifyCodeRequestTestDataBuilder.verifyCodeRequestBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메일 인증 코드 요청 본문 검증 테스트")
public class VerifyCodeRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        VerifyCodeRequest request = verifyCodeRequestBuilder().build();

        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 이메일이_빈_문자열이면_검증에_실패한다() {
        VerifyCodeRequest request = verifyCodeRequestBuilder().withEmail("").build();

        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일은 필수 입력 값입니다");
    }

    @Test
    void 이메일이_null이면_검증에_실패한다() {
        VerifyCodeRequest request = verifyCodeRequestBuilder().withEmail(null).build();

        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일은 필수 입력 값입니다");
    }

    @Test
    void 인증코드가_빈_문자열이면_검증에_실패한다() {
        VerifyCodeRequest request = verifyCodeRequestBuilder().withCode("").build();

        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(2);

        Set<String> messages =
                violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());
        assertThat(messages)
                .containsExactlyInAnyOrder(
                        "인증 코드는 필수 입력 값입니다", "인증 코드는 100000에서 999999 사이의 6자리 숫자여야 합니다");
    }

    @Test
    void 인증코드가_null이면_검증에_실패한다() {
        VerifyCodeRequest request = verifyCodeRequestBuilder().withCode(null).build();

        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(2);

        Set<String> messages =
                violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toSet());
        assertThat(messages)
                .containsExactlyInAnyOrder(
                        "인증 코드는 필수 입력 값입니다", "인증 코드는 100000에서 999999 사이의 6자리 숫자여야 합니다");
    }

    @Test
    void 인증코드가_100000에서_999999_숫자가_아니면_검증에_실패한다() {
        VerifyCodeRequest request = verifyCodeRequestBuilder().withCode("012345").build();

        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("인증 코드는 100000에서 999999 사이의 6자리 숫자여야 합니다");
    }
}
