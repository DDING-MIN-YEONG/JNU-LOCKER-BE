package com.jnulocker.auth.application.port.in.request;

import static auth.application.port.in.request.SendEmailRequestTestDataBuilder.sendEmailRequestBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메일 전송 요청 본문 검증 테스트")
public class SendEmailRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        SendEmailRequest request = sendEmailRequestBuilder().build();

        Set<ConstraintViolation<SendEmailRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 이메일이_빈_문자열이면_검증에_실패한다() {
        SendEmailRequest request = sendEmailRequestBuilder().withEmail("").build();

        Set<ConstraintViolation<SendEmailRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일은 필수 입력 값입니다");
    }

    @Test
    void 이메일이_null이면_검증에_실패한다() {
        SendEmailRequest request = sendEmailRequestBuilder().withEmail(null).build();

        Set<ConstraintViolation<SendEmailRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일은 필수 입력 값입니다");
    }
}
