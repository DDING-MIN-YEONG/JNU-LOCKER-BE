package com.jnulocker.auth.application.port.in.request;

import static auth.application.port.in.request.LoginRequestTestDataBuilder.loginRequestBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LOGIN 요청 본문 검증 테스트")
class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        LoginRequest request = loginRequestBuilder().build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 이메일이_null이면_검증에_실패한다() {
        LoginRequest request = loginRequestBuilder().withEmail(null).build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이메일은 필수 입력값입니다"));
    }

    @Test
    void 이메일이_공백이면_검증에_실패한다() {
        LoginRequest request = loginRequestBuilder().withEmail("").build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이메일은 필수 입력값입니다"));
    }

    @Test
    void 이메일이_형식에_맞지_않으면_검증에_실패한다() {
        LoginRequest request = loginRequestBuilder().withEmail("abcde12345").build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("올바른 형식의 이메일 주소여야 합니다"));
    }

    @Test
    void 비밀번호가_null이면_검증에_실패한다() {
        LoginRequest request = loginRequestBuilder().withPassword(null).build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("비밀번호는 필수 입력값입니다"));
    }

    @Test
    void 비밀번호가_공백이면_검증에_실패한다() {
        LoginRequest request = loginRequestBuilder().withPassword("").build();

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("비밀번호는 필수 입력값입니다"));
    }
}
