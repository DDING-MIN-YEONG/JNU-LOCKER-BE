package com.jnulocker.auth.adapter.in;

import static auth.application.port.in.request.ManagerSignupRequestTestDataBuilder.managerSignupRequestBuilder;
import static auth.application.port.in.request.UserSignupRequestTestDataBuilder.userSignupRequestBuilder;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.jnulocker.auth.adapter.out.TokenRepository;
import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.response.AuthToken;
import com.jnulocker.auth.exception.AuthErrorCode;
import com.jnulocker.auth.jwt.RefreshToken;
import com.jnulocker.auth.jwt.exception.JwtErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.member.adapter.out.MemberRepository;
import com.jnulocker.member.domain.Member;
import com.jnulocker.organization.adapter.out.DepartmentRepository;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.domain.Organization;
import com.jnulocker.organization.exception.DepartmentErrorCode;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
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
import organization.builder.DepartmentTestDataBuilder;
import organization.builder.OrganizationTestDataBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("인증인가 통합 테스트")
class AuthControllerIntegrationTest {

    private static final String AUTH_URL = "/v1/auth";

    @LocalServerPort private int port;

    @Autowired private MemberRepository memberRepository;

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private DepartmentRepository departmentRepository;

    @Autowired private TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String secretKey;

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
        departmentRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    // USER 회원가입 테스트
    @Test
    void USER_회원가입을_할_수_있다() {
        // given
        Department department = setDepartment();
        UserSignupRequest request =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();

        // when
        ValidatableResponse response = signupUser(port, request);

        // then
        response.statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void USER_존재하지_않는_학과로_회원가입하면_Department_Not_Found_에러_응답을_받는다() {
        // given
        Long nonexistentDepartmentId = -1L;
        UserSignupRequest request =
                userSignupRequestBuilder().withDepartmentId(nonexistentDepartmentId).build();

        // when
        ErrorResponse errorResponse =
                signupUser(port, request)
                        .statusCode(
                                DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getMessage());
    }

    @Test
    void USER_동일메일로_회원가입하면_User_Already_Exist_에러_응답을_받는다() {
        // given
        Department department = setDepartment();
        UserSignupRequest request =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();

        // when
        signupUser(port, request).statusCode(HttpStatus.CREATED.value());
        ErrorResponse errorResponse =
                signupUser(port, request)
                        .statusCode(AuthErrorCode.USER_ALREADY_EXIST.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AuthErrorCode.USER_ALREADY_EXIST.getMessage());
    }

    // MANAGER 회원가입 테스트
    @Test
    void MANAGER_회원가입을_할_수_있다() {
        // given
        Department department = setDepartment();
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withDepartmentId(department.getId()).build();

        // when
        ValidatableResponse response = signupManager(port, request);

        // then
        response.statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void MANAGER_존재하지_않는_학과로_회원가입하면_Department_Not_Found_에러_응답을_받는다() {
        // given
        Long nonexistentDepartmentId = -1L;
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withDepartmentId(nonexistentDepartmentId).build();

        // when
        ErrorResponse errorResponse =
                signupManager(port, request)
                        .statusCode(
                                DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(DepartmentErrorCode.DEPARTMENT_NOT_FOUND.getMessage());
    }

    @Test
    void MANAGER_동일메일로_회원가입하면_User_Already_Exist_에러_응답을_받는다() {
        // given
        Department department = setDepartment();
        ManagerSignupRequest request =
                managerSignupRequestBuilder().withDepartmentId(department.getId()).build();

        // when
        signupManager(port, request).statusCode(HttpStatus.CREATED.value());
        ErrorResponse errorResponse =
                signupManager(port, request)
                        .statusCode(AuthErrorCode.USER_ALREADY_EXIST.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AuthErrorCode.USER_ALREADY_EXIST.getMessage());
    }

    @Test
    void USER_로그인_성공시_토큰을_반환한다() {
        // given
        Department department = setDepartment();
        UserSignupRequest signupRequest =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupUser(port, signupRequest);

        LoginRequest loginRequest =
                new LoginRequest(signupRequest.email(), signupRequest.password());

        // when
        AuthToken authToken =
                loginUser(port, loginRequest)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(AuthToken.class);

        // then
        assertThat(authToken.accessToken()).isNotBlank();
        assertThat(authToken.refreshToken()).isNotBlank();
    }

    @Test
    void 토큰_재발급_요청시_새로운_AccessToken을_받는다() {
        // given
        Department department = setDepartment();
        UserSignupRequest signupRequest =
                userSignupRequestBuilder().withDepartmentId(department.getId()).build();
        signupUser(port, signupRequest);

        LoginRequest loginRequest =
                new LoginRequest(signupRequest.email(), signupRequest.password());
        AuthToken authToken =
                loginUser(port, loginRequest)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(AuthToken.class);

        // when
        AuthToken reissuedToken =
                reissueToken(port, authToken.refreshToken())
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(AuthToken.class);

        // then
        assertThat(reissuedToken.accessToken()).isNotBlank();
    }

    @Test
    void 유효하지_않은_리프레시_토큰으로_재발급하면_InvalidRefreshToken_에러가_발생한다() {
        // given
        String invalidRefreshToken = "invalid.refresh.token";

        // when
        ErrorResponse errorResponse =
                reissueToken(port, invalidRefreshToken)
                        .statusCode(JwtErrorCode.INVALID_REFRESH_TOKEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(JwtErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }

    @Test
    void 만료된_토큰으로_재발급하면_Token_Expired_에러가_발생한다() {
        Member member = createTestMember();
        String expiredRefreshToken = createExpiredRefreshToken(member.getId(), secretKey);
        RefreshToken refreshToken = new RefreshToken(member.getId(), expiredRefreshToken, -1000L);
        tokenRepository.save(refreshToken);

        // when
        ErrorResponse errorResponse =
                given().port(port)
                        .queryParam("refreshToken", expiredRefreshToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(AUTH_URL + "/reissue")
                        .then()
                        .statusCode(JwtErrorCode.EXPIRED_TOKEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then

        assertThat(errorResponse.message()).isEqualTo(JwtErrorCode.EXPIRED_TOKEN.getMessage());
    }

    public static ValidatableResponse loginUser(int port, LoginRequest request) {
        return given().port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/login")
                .then()
                .log()
                .all();
    }

    public static ValidatableResponse reissueToken(int port, String refreshToken) {
        return given().port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("refreshToken", refreshToken)
                .when()
                .post(AUTH_URL + "/reissue")
                .then()
                .log()
                .all();
    }

    Department setDepartment() {
        Organization organization = OrganizationTestDataBuilder.builder().build();
        Organization savedOrganization = organizationRepository.save(organization);
        Department department =
                DepartmentTestDataBuilder.builder().withOrganization(savedOrganization).build();
        departmentRepository.save(department);
        return department;
    }

    public static ValidatableResponse signupUser(int port, UserSignupRequest request) {
        return given().port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/users/signup")
                .then()
                .log()
                .all();
    }

    public static ValidatableResponse signupManager(int port, ManagerSignupRequest request) {
        return given().port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(AUTH_URL + "/managers/signup")
                .then()
                .log()
                .all();
    }

    public static String createExpiredRefreshToken(Long userId, String secretKey) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now - 2000);
        Date expiredAt = new Date(now - 1000);

        return Jwts.builder()
                .claim("id", userId)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    private Member createTestMember() {
        Organization organization = OrganizationTestDataBuilder.builder().build();
        Organization savedOrganization = organizationRepository.save(organization);
        Department department =
                DepartmentTestDataBuilder.builder().withOrganization(savedOrganization).build();
        departmentRepository.save(department);

        Member member =
                Member.createManager(
                        "test", "test@example.com", "test12345!", "010-1234-1234", department);
        return memberRepository.save(member);
    }
}
