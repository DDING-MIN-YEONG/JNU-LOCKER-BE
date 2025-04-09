package com.jnulocker.auth.application.port.in.request;

import static auth.application.port.in.request.ManagerSignupRequestTestDataBuilder.managerSignupRequestBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MANAGER 회원가입 요청 본문 검증 테스트")
class ManagerSignupRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 이름이_null이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withName(null).build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이름은 필수입니다."));
    }

    @Test
    void 이름이_공백이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withName("").build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이름은 필수입니다."));
    }

    @Test
    void 이메일이_null이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withEmail(null).build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이메일은 필수입니다."));
    }

    @Test
    void 이메일이_공백이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withEmail("").build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이메일은 필수입니다."));
    }

    @Test
    void 이메일이_형식에_맞지_않으면_검증에_실패한다() {
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withEmail("invalid-email").build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("올바른 이메일 형식이 아닙니다."));
    }

    @Test
    void 비밀번호가_null이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withPassword(null).build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("비밀번호는 필수입니다."));
    }

    @Test
    void 비밀번호가_공백이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withPassword("").build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("비밀번호는 필수입니다."));
    }

    @Test
    void 비밀번호가_형식에_맞지_않으면_검증에_실패한다() {
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withPassword("password1").build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations)
                .anyMatch(
                        v ->
                                v.getMessage()
                                        .equals(
                                                "비밀번호는 영문, 숫자, 특수문자를 각각 최소 1자 이상 포함하고 9자 이상이어야 합니다."));
    }

    @Test
    void departmentId가_null이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withDepartmentId(null).build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("소속은 필수입니다."));
    }

    @Test
    void 전화번호가_null이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withPhoneNumber(null).build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전화번호는 필수입니다."));
    }

    @Test
    void 전화번호가_공백이면_검증에_실패한다() {
        ManagerSignupRequest request = managerSignupRequestBuilder().withPhoneNumber("").build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전화번호는 필수입니다."));
    }

    @Test
    void 전화번호_형식이_잘못되면_검증에_실패한다() {
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withPhoneNumber("01012341234").build();

        Set<ConstraintViolation<ManagerSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전화번호 형식이 올바르지 않습니다."));
    }
}
