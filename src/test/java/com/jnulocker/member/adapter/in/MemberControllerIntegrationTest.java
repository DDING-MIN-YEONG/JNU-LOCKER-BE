package com.jnulocker.member.adapter.in;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@DisplayName("회원 통합 테스트")
public class MemberControllerIntegrationTest {

    private static final String MEMBER_URL = "/v1/members";
    private static final String ACCESS_TOKEN = "access_token";

    @LocalServerPort private int port;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private AuthTestUtil authTestUtil;

    private static String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // 토큰 생성
        accessToken = authTestUtil.generateAccessToken(Role.USER);
    }

    @AfterEach
    void tearDown() {
        memberTestUtil.deleteAll();
    }

    @Test
    void 회원_정보를_조회할_수_있다() {
        // given
        Member expected = memberTestUtil.createUser();

        // when
        MemberInfoResponse actual =
                getMemberInfo(accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MemberInfoResponse.class);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.name()).isEqualTo(expected.getName());
        assertThat(actual.studentNumber()).isEqualTo(expected.getStudentNumber());
        assertThat(actual.affiliation()).isEqualTo(expected.getDepartment().getName());
        assertThat(actual.phoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.email()).isEqualTo(expected.getEmail());
    }

    public static ValidatableResponse getMemberInfo(String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(MEMBER_URL + "/info")
                .then()
                .log()
                .all();
    }
}
