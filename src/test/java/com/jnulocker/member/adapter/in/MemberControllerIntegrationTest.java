package com.jnulocker.member.adapter.in;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.member.application.port.in.response.MemberInfoResponse;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.utils.OrganizationUtil;
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

    @Autowired private OrganizationUtil organizationUtil;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        memberTestUtil.deleteAll();
    }

    @Test
    void USER_회원_정보를_조회할_수_있다() {
        // given
        Member expectedMember = memberTestUtil.createUser();
        String accessToken =
                authTestUtil.generateAccessTokenFromMemberId(
                        expectedMember.getId(), expectedMember.getRole());

        // when
        MemberInfoResponse memberInfoResponse =
                getMemberInfo(accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MemberInfoResponse.class);

        // then
        assertThat(memberInfoResponse).isNotNull();
        assertThat(memberInfoResponse.memberId()).isEqualTo(expectedMember.getId());
        assertThat(memberInfoResponse.name()).isEqualTo(expectedMember.getName());
        assertThat(memberInfoResponse.studentNumber()).isEqualTo(expectedMember.getStudentNumber());
        assertThat(memberInfoResponse.affiliation())
                .isEqualTo(expectedMember.getDepartment().getName());
        assertThat(memberInfoResponse.phoneNumber()).isEqualTo(expectedMember.getPhoneNumber());
        assertThat(memberInfoResponse.email()).isEqualTo(expectedMember.getEmail());
    }

    @Test
    void MANAGER_COUNCIL_회원_정보를_조회할_수_있다() {
        // given
        Department council = organizationUtil.createCouncilDepartment();
        Member expectedMember =
                memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, council);
        String accessToken =
                authTestUtil.generateAccessTokenFromMemberId(
                        expectedMember.getId(), expectedMember.getRole());

        // when
        MemberInfoResponse memberInfoResponse =
                getMemberInfo(accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MemberInfoResponse.class);

        // then
        assertThat(memberInfoResponse).isNotNull();
        assertThat(memberInfoResponse.memberId()).isEqualTo(expectedMember.getId());
        assertThat(memberInfoResponse.name()).isEqualTo(expectedMember.getName());
        assertThat(memberInfoResponse.studentNumber()).isEqualTo(expectedMember.getStudentNumber());
        assertThat(memberInfoResponse.affiliation())
                .isEqualTo(expectedMember.getDepartment().getNickname());
        assertThat(memberInfoResponse.phoneNumber()).isEqualTo(expectedMember.getPhoneNumber());
        assertThat(memberInfoResponse.email()).isEqualTo(expectedMember.getEmail());
    }

    @Test
    void MANAGER_COMMITTEE_회원_정보를_조회할_수_있다() {
        // given
        Department committee = organizationUtil.createCommitteeDepartment();
        Member expectedMember =
                memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, committee);
        String accessToken =
                authTestUtil.generateAccessTokenFromMemberId(
                        expectedMember.getId(), expectedMember.getRole());

        // when
        MemberInfoResponse memberInfoResponse =
                getMemberInfo(accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MemberInfoResponse.class);

        // then
        assertThat(memberInfoResponse).isNotNull();
        assertThat(memberInfoResponse.memberId()).isEqualTo(expectedMember.getId());
        assertThat(memberInfoResponse.name()).isEqualTo(expectedMember.getName());
        assertThat(memberInfoResponse.studentNumber()).isEqualTo(null);
        assertThat(memberInfoResponse.affiliation())
                .isEqualTo(expectedMember.getDepartment().getNickname());
        assertThat(memberInfoResponse.phoneNumber()).isEqualTo(expectedMember.getPhoneNumber());
        assertThat(memberInfoResponse.email()).isEqualTo(expectedMember.getEmail());
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
