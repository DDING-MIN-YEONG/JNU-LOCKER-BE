package com.jnulocker.auth.adapter.in;

import static auth.application.port.in.request.ManagerSignupRequestTestDataBuilder.managerSignupRequestBuilder;
import static auth.application.port.in.request.SendEmailRequestTestDataBuilder.sendEmailRequestBuilder;
import static auth.application.port.in.request.UserSignupRequestTestDataBuilder.userSignupRequestBuilder;
import static auth.application.port.in.request.VerifyCodeRequestTestDataBuilder.verifyCodeRequestBuilder;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.jnulocker.auth.adapter.out.TokenRepository;
import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.SendEmailRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;
import com.jnulocker.auth.exception.AuthErrorCode;
import com.jnulocker.auth.jwt.RefreshToken;
import com.jnulocker.auth.jwt.exception.JwtErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.common.util.RedisUtil;
import com.jnulocker.member.adapter.out.MemberRepository;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.exception.MemberErrorCode;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.exception.DepartmentErrorCode;
import com.jnulocker.organization.utils.OrganizationUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("인증 통합 테스트")
class AuthControllerIntegrationTest {

    private static final String AUTH_URL = "/v1/auth";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    @LocalServerPort private int port;

    @Autowired private MemberRepository memberRepository;

    @Autowired private TokenRepository tokenRepository;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private OrganizationUtil organizationUtil;

    @Autowired private RedisUtil redisUtil;

    @Value("${custom.jwt.access-secret-key}")
    String accessSecretKey;

    @Value("${custom.jwt.refresh-secret-key}")
    private String refreshSecretKey;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        clearData();
    }

    @AfterEach
    void tearDown() {
        clearData();
    }

    private void clearData() {
        memberRepository.deleteAll();
    }

    // USER 회원가입 테스트 (변경 없음)
    @Test
    void USER_회원가입을_할_수_있다() {
        Department department = organizationUtil.createDepartment();
        UserSignupRequest request =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        ValidatableResponse response = signupUser(request);
        response.statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void USER_존재하지_않는_학과로_회원가입하면_Department_Not_Found_에러_응답을_받는다() {
        Long nonexistentDepartmentId = -1L;
        UserSignupRequest request =
                userSignupRequestBuilder().withDepartmentId(nonexistentDepartmentId).build();
        ErrorResponse errorResponse =
                signupUser(request)
                        .statusCode(
                                DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);
        assertThat(errorResponse.message())
                .isEqualTo(DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getMessage());
    }

    @Test
    void USER_동일메일로_회원가입하면_User_Already_Exist_에러_응답을_받는다() {
        Department department = organizationUtil.createDepartment();
        UserSignupRequest request =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupUser(request).statusCode(HttpStatus.CREATED.value());
        ErrorResponse errorResponse =
                signupUser(request)
                        .statusCode(AuthErrorCode.USER_ALREADY_EXIST.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);
        assertThat(errorResponse.message())
                .isEqualTo(AuthErrorCode.USER_ALREADY_EXIST.getMessage());
    }

    @Test
    void MANAGER_회원가입을_할_수_있다() {
        Department department = organizationUtil.createDepartment();
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withDepartmentId(department.getId()).build();
        ValidatableResponse response = signupManager(request);
        response.statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void MANAGER_학생회_회원가입_시_학번이_null이면_Student_Number_Required_에러_응답을_받는다() {
        Department department = organizationUtil.createDepartment();
        ManagerSignupRequest request =
                managerSignupRequestBuilder()
                        .withDepartmentId(department.getId())
                        .withStudentNumber(null)
                        .build();
        ErrorResponse errorResponse =
                signupManager(request)
                        .statusCode(AuthErrorCode.STUDENT_NUMBER_REQUIRED.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);
        assertThat(errorResponse.message())
                .isEqualTo(AuthErrorCode.STUDENT_NUMBER_REQUIRED.getMessage());
    }

    @Test
    void MANAGER_학생회_회원가입_시_학번이_입력되면_정상적으로_회원가입된다() {
        Department department = organizationUtil.createDepartment();
        ManagerSignupRequest request =
                managerSignupRequestBuilder()
                        .withDepartmentId(department.getId())
                        .withStudentNumber("221965")
                        .build();
        ValidatableResponse response = signupManager(request);
        response.statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void MANAGER_자치회_회원가입_시_학번이_입력되지_않으면_정상적으로_회원가입된다() {
        Department department = organizationUtil.createCommitteeDepartment();
        ManagerSignupRequest request =
                managerSignupRequestBuilder()
                        .withDepartmentId(department.getId())
                        .withStudentNumber(null)
                        .build();
        ValidatableResponse response = signupManager(request);
        response.statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void MANAGER_존재하지_않는_학과로_회원가입하면_Department_Not_Found_에러_응답을_받는다() {
        Long nonexistentDepartmentId = -1L;
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withDepartmentId(nonexistentDepartmentId).build();
        ErrorResponse errorResponse =
                signupManager(request)
                        .statusCode(
                                DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);
        assertThat(errorResponse.message())
                .isEqualTo(DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getMessage());
    }

    @Test
    void MANAGER_동일메일로_회원가입하면_User_Already_Exist_에러_응답을_받는다() {
        Department department = organizationUtil.createDepartment();
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupManager(request).statusCode(HttpStatus.CREATED.value());
        ErrorResponse errorResponse =
                signupManager(request)
                        .statusCode(AuthErrorCode.USER_ALREADY_EXIST.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);
        assertThat(errorResponse.message())
                .isEqualTo(AuthErrorCode.USER_ALREADY_EXIST.getMessage());
    }

    @Test
    void 로그인_성공시_토큰을_반환한다() {
        // given
        Department department = organizationUtil.createDepartment();
        UserSignupRequest signupRequest =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupUser(signupRequest);

        LoginRequest loginRequest =
                new LoginRequest(signupRequest.email(), signupRequest.password());

        // when
        ExtractableResponse<Response> response =
                loginUser(loginRequest).statusCode(HttpStatus.OK.value()).extract();

        // then
        Cookies cookies = response.detailedCookies();
        String accessToken = getCookieValue(cookies, ACCESS_TOKEN);
        String refreshToken = getCookieValue(cookies, REFRESH_TOKEN);

        assertThat(accessToken).isNotBlank();
        assertThat(refreshToken).isNotBlank();
    }

    @Test
    void 토큰_재발급_요청시_새로운_AccessToken을_받는다() {
        // given
        Department department = organizationUtil.createDepartment();
        UserSignupRequest signupRequest =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupUser(signupRequest);

        LoginRequest loginRequest =
                new LoginRequest(signupRequest.email(), signupRequest.password());
        ExtractableResponse<Response> loginResponse =
                loginUser(loginRequest).statusCode(HttpStatus.OK.value()).extract();
        String refreshToken = getCookieValue(loginResponse.detailedCookies(), REFRESH_TOKEN);

        // when
        ExtractableResponse<Response> response =
                reissueToken(refreshToken).statusCode(HttpStatus.OK.value()).extract();

        // then
        String newAccessToken = getCookieValue(response.detailedCookies(), ACCESS_TOKEN);
        assertThat(newAccessToken).isNotBlank();
    }

    @Test
    void 유효하지_않은_리프레시_토큰으로_재발급하면_InvalidRefreshToken_에러가_발생한다() {
        // given
        String invalidRefreshToken = "invalid.refresh.token";

        // when
        ErrorResponse errorResponse =
                reissueToken(invalidRefreshToken)
                        .statusCode(JwtErrorCode.INVALID_REFRESH_TOKEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(JwtErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    void 만료된_리프레시토큰으로_재발급하면_Token_Expired_에러가_발생한다() {
        // given
        Member member = memberTestUtil.createManager();
        String expiredRefreshToken = createExpiredToken(member.getId(), refreshSecretKey);
        RefreshToken refreshToken = new RefreshToken(member.getId(), expiredRefreshToken, -1000L);
        tokenRepository.save(refreshToken);

        // when
        ErrorResponse errorResponse =
                reissueToken(expiredRefreshToken)
                        .statusCode(JwtErrorCode.EXPIRED_TOKEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(JwtErrorCode.EXPIRED_TOKEN.getMessage());
    }

    @Test
    void 만료된_액세스토큰으로_요청하면_Token_Expired_에러가_발생한다() {
        // given
        Member member = memberTestUtil.createManager();
        String expiredAccessToken = createExpiredToken(member.getId(), accessSecretKey);

        // when
        ErrorResponse errorResponse =
                given().contentType(MediaType.APPLICATION_JSON_VALUE)
                        .cookie(new Cookie.Builder(ACCESS_TOKEN, expiredAccessToken).build())
                        .when()
                        .get("/v1/events")
                        .then()
                        .statusCode(JwtErrorCode.EXPIRED_TOKEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(JwtErrorCode.EXPIRED_TOKEN.getMessage());
        assertThat(errorResponse.code()).isEqualTo(JwtErrorCode.EXPIRED_TOKEN.getCode());
    }

    @Test
    void 액세스토큰이_null이면_InvalidAccessTokenException이_발생한다() {
        // given
        String nullAccessToken = null;

        // when
        ErrorResponse errorResponse =
                given().contentType(MediaType.APPLICATION_JSON_VALUE)
                        .cookie(new Cookie.Builder(ACCESS_TOKEN, nullAccessToken).build())
                        .when()
                        .get("/v1/events")
                        .then()
                        .statusCode(JwtErrorCode.INVALID_ACCESS_TOKEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(JwtErrorCode.INVALID_ACCESS_TOKEN.getMessage());
        assertThat(errorResponse.code()).isEqualTo(JwtErrorCode.INVALID_ACCESS_TOKEN.getCode());
    }

    @Test
    void 올바르지_않은_이메일로_로그인하면_인증에_실패한다() {
        // given
        Department department = organizationUtil.createDepartment();
        UserSignupRequest signupRequest =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupUser(signupRequest);

        LoginRequest loginRequest = new LoginRequest("123456@jnu.ac.kr", signupRequest.password());

        // when
        ErrorResponse errorResponse =
                loginUser(loginRequest)
                        .statusCode(MemberErrorCode.MEMBER_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 올바르지_않은_비밀번호로_로그인하면_인증에_실패한다() {
        // given
        Department department = organizationUtil.createDepartment();
        UserSignupRequest signupRequest =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupUser(signupRequest);

        LoginRequest loginRequest = new LoginRequest(signupRequest.email(), "wrong12345!");

        // when
        ErrorResponse errorResponse =
                loginUser(loginRequest)
                        .statusCode(AuthErrorCode.FAIL_AUTHENTICATION.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AuthErrorCode.FAIL_AUTHENTICATION.getMessage());
    }

    @Test
    void 이메일_전송_요청시_성공한다() {
        // given
        SendEmailRequest request = sendEmailRequestBuilder().build();

        // when
        ValidatableResponse response = sendEmail(request);

        // then
        response.statusCode(HttpStatus.OK.value());
    }

    @Test
    void 인증코드_검증_요청시_성공한다() {
        // given
        String email = "test@example.com";
        String code = "123456";
        VerifyCodeRequest request =
                verifyCodeRequestBuilder().withEmail(email).withCode(code).build();
        redisUtil.setDataExpire(email, code, 300);

        // when
        ValidatableResponse response = verify(request);

        // then
        response.statusCode(HttpStatus.OK.value());
    }

    @Test
    void 인증코드가_일치하지_않으면_Code_Not_Correct_에러_응답을_받는다() {
        // given
        String email = "test@example.com";
        String incorrectCode = "12347";
        redisUtil.setDataExpire(email, incorrectCode, 300);

        VerifyCodeRequest request =
                verifyCodeRequestBuilder().withEmail(email).withCode("123456").build();

        // when
        ErrorResponse errorResponse =
                verify(request)
                        .statusCode(AuthErrorCode.CODE_NOT_CORRECT.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(AuthErrorCode.CODE_NOT_CORRECT.getMessage());
    }

    @Test
    void 만료된_인증코드를_사용하면_Code_Ttl_Expired_에러_응답을_받는다() {
        // given
        String email = "test@example.com";
        String expiredCode = "123456";
        redisUtil.deleteData(email);

        VerifyCodeRequest request =
                verifyCodeRequestBuilder().withEmail(email).withCode(expiredCode).build();

        // when
        ErrorResponse errorResponse =
                verify(request)
                        .statusCode(AuthErrorCode.CODE_TTL_EXPIRED.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(AuthErrorCode.CODE_TTL_EXPIRED.getMessage());
    }

    public static ValidatableResponse loginUser(LoginRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/login")
                .then()
                .log()
                .all();
    }

    public static ValidatableResponse reissueToken(String refreshToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(REFRESH_TOKEN, refreshToken).build()) // 쿠키로 전송
                .when()
                .post(AUTH_URL + "/reissue")
                .then()
                .log()
                .all();
    }

    private String getCookieValue(Cookies cookies, String name) {
        return cookies.getValue(name);
    }

    public static ValidatableResponse signupUser(UserSignupRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/users/signup")
                .then()
                .log()
                .all();
    }

    public static ValidatableResponse signupManager(ManagerSignupRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/managers/signup")
                .then()
                .log()
                .all();
    }

    public static ValidatableResponse sendEmail(SendEmailRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/send-email")
                .then()
                .log()
                .all();
    }

    public static ValidatableResponse verify(VerifyCodeRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/verify")
                .then()
                .log()
                .all();
    }

    public static String createExpiredToken(Long userId, String secretKey) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now - 2000);
        Date expiredAt = new Date(now - 1000);

        return Jwts.builder()
                .claim("id", userId)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
