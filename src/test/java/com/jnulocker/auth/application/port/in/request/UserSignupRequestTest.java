package com.jnulocker.auth.application.port.in.request;

import static auth.application.port.in.request.UserSignupRequestTestDataBuilder.userSignupRequestBuilder;
import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("USER 회원가입 요청 본문 검증 테스트")
class UserSignupRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_요청은_검증을_통과한다() {
        UserSignupRequest request = userSignupRequestBuilder().build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void 이름이_빈_문자열이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withName("").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이름은 필수입니다.");
    }

    @Test
    void 이름이_null이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withName(null).build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이름은 필수입니다.");
    }

    @Test
    void departmentId가_null이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withDepartmentId(null).build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("소속은 필수입니다.");
    }

    @Test
    void 이메일이_null이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withEmail(null).build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일은 필수입니다.");
    }

    @Test
    void 이메일이_공백이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withEmail("").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("이메일은 필수입니다."));
    }

    @Test
    void 이메일이_형식에_맞지_않으면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withEmail("invalid-email").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("올바른 이메일 형식이 아닙니다."));
    }

    @Test
    void 이메일이_전남대_도메인이_아니면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withEmail("test@gmail.com").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전남대학교 메일이 아닙니다."));
    }

    @Test
    void 비밀번호가_null이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withPassword(null).build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("비밀번호는 필수입니다."));
    }

    @Test
    void 비밀번호가_공백이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withPassword("").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("비밀번호는 필수입니다."));
    }

    @Test
    void 비밀번호가_형식에_맞지_않으면_검증에_실패한다() {
        // 특수문자 없음, 9자 미만
        UserSignupRequest request = userSignupRequestBuilder().withPassword("abc12345").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations)
                .anyMatch(
                        v ->
                                v.getMessage()
                                        .equals(
                                                "비밀번호는 영문, 숫자, 특수문자를 각각 최소 1자 이상 포함하고 9자 이상이어야 합니다."));
    }

    @Test
    void 전화번호가_null이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withPhoneNumber(null).build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전화번호는 필수입니다."));
    }

    @Test
    void 전화번호가_공백이면_검증에_실패한다() {
        UserSignupRequest request = userSignupRequestBuilder().withPhoneNumber("").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전화번호는 필수입니다."));
    }

    @Test
    void 전화번호_형식이_잘못되면_검증에_실패한다() {
        UserSignupRequest request =
                userSignupRequestBuilder().withPhoneNumber("01012341234").build();

        Set<ConstraintViolation<UserSignupRequest>> violations = validator.validate(request);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("전화번호 형식이 올바르지 않습니다."));
    }
}
