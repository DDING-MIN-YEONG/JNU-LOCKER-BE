package com.jnulocker.announce.adapter.in;

import static announce.application.port.in.request.CreateAnnounceRequestTestDataBuilder.createAnnounceRequestBuilder;
import static announce.application.port.in.request.UpdateAnnounceRequestTestDataBuilder.updateAnnounceRequestBuilder;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.announce.application.port.in.request.CreateAnnounceRequest;
import com.jnulocker.announce.application.port.in.request.UpdateAnnounceRequest;
import com.jnulocker.announce.application.port.in.response.AnnounceCustomPage;
import com.jnulocker.announce.application.port.in.response.MyAnnounceResponse;
import com.jnulocker.announce.exception.AnnounceErrorCode;
import com.jnulocker.announce.utils.AnnounceTestUtil;
import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.utils.OrganizationTestUtil;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
@DisplayName("공지사항 컨트롤러 통합 테스트")
public class AnnounceControllerIntegrationTest {

    private static final String ANNOUNCE_URL = "/v1/announces";
    private static final String ACCESS_TOKEN = "access_token";

    @LocalServerPort private int port;

    @Autowired private AuthTestUtil authTestUtil;

    @Autowired private OrganizationTestUtil organizationTestUtil;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private AnnounceTestUtil announceTestUtil;

    private static String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // 토큰 생성
        accessToken = authTestUtil.generateAccessToken(Role.MANAGER);
    }

    @AfterEach
    void tearDown() {
        memberTestUtil.deleteAll();
        announceTestUtil.deleteAll();
    }

    @Test
    void 공지사항을_생성할_수_있다() {
        // given
        createAnnounce();

        // then
        // 생성된 공지사항 조회
        AnnounceCustomPage announceCustomPage =
                getAnnounces(0, 10, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(AnnounceCustomPage.class);

        // 생성된 공지사항이 목록에 포함되어 있는지 확인
        assertThat(announceCustomPage.content()).hasSize(1);
    }

    // 공지사항 목록 조회 테스트
    @ParameterizedTest(name = "[{index}] 조회[총: {0}, 크기: {1}, 페이지: {2}] -> 마지막: {3}")
    @CsvSource({
        "10, 5, 0, false", // 10개 생성, 5개씩, 0페이지(1~5), 마지막 아님
        "10, 5, 1, true", // 10개 생성, 5개씩, 1페이지(6~10), 마지막
        "8, 3, 0, false", // 8개 생성, 3개씩, 0페이지(1~3), 마지막 아님
        "8, 3, 2, true", // 8개 생성, 3개씩, 2페이지(7~8), 마지막
        "5, 10, 0, true" // 5개 생성, 10개씩, 0페이지(1~5), 마지막
    })
    void 공지사항_목록을_조회할_수_있다(int createCount, int pageSize, int page, boolean expectedLast) {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        for (int i = 0; i < createCount; i++) {
            announceTestUtil.createAnnounceWithParticipationDepartment(department);
        }

        // when
        AnnounceCustomPage announceCustomPage =
                getAnnounces(page, pageSize, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(AnnounceCustomPage.class);

        // then
        int expectedSize = calculateExpectedSize(createCount, pageSize, page);
        assertThat(announceCustomPage.content()).hasSize(expectedSize);
        assertThat(announceCustomPage.totalElements()).isEqualTo(createCount);
        assertThat(announceCustomPage.last()).isEqualTo(expectedLast);
    }

    @Test
    void 자신이_속한_학과가_참여하는_공지사항을_조회할_수_있다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);

        // 공지사항 생성
        announceTestUtil.createAnnounceWithParticipationDepartment(department);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        List<MyAnnounceResponse> myAnnounces =
                getMyAnnounces(accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath()
                        .getList(".", MyAnnounceResponse.class);

        // then
        assertThat(myAnnounces).hasSize(1);
    }

    @Test
    void 자신의_소속학과가_주관하지_않는_공지사항은_조회할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // 공지사항 생성: 비참여 학과로 생성
        announceTestUtil.createAnnounce();

        // when
        List<MyAnnounceResponse> myAnnounces =
                getMyAnnounces(accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath()
                        .getList(".", MyAnnounceResponse.class);

        // then
        assertThat(myAnnounces).isEmpty();
    }

    @Test
    void 공지사항을_삭제할_수_있다() {
        // given
        createAnnounce();

        // 생성된 공지사항 조회
        Long announceId = getLastAnnounceId();

        // when
        deleteAnnounce(announceId).statusCode(HttpStatus.NO_CONTENT.value());

        // then
        // 삭제된 공지사항이 더 이상 조회되지 않는지 확인
        AnnounceCustomPage announceCustomPage =
                getAnnounces(0, 10, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(AnnounceCustomPage.class);

        assertThat(announceCustomPage.content())
                .noneMatch(announce -> announce.id().equals(announceId));
    }

    @Test
    void 존재하지_않는_공지사항은_삭제할_수_없다() {
        // given
        Long nonExistentAnnounceId = System.currentTimeMillis();

        // when
        ErrorResponse errorResponse =
                deleteAnnounce(nonExistentAnnounceId)
                        .statusCode(AnnounceErrorCode.ANNOUNCE_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AnnounceErrorCode.ANNOUNCE_NOT_FOUND.getMessage());
    }

    @Test
    void 권한이_없는_사용자는_공지사항을_삭제할_수_없다() {
        // given
        createAnnounce();

        // 생성된 공지사항 조회
        Long announceId = getLastAnnounceId();

        // 비조직원으로 로그인
        accessToken = authTestUtil.generateAccessTokenWithAnotherDepartment(Role.MANAGER);

        // when
        ErrorResponse errorResponse =
                deleteAnnounce(announceId)
                        .statusCode(
                                AnnounceErrorCode.ONLY_MANAGER_CAN_DELETE_ANNOUNCE
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AnnounceErrorCode.ONLY_MANAGER_CAN_DELETE_ANNOUNCE.getMessage());
    }

    @Test
    void 공지사항을_수정할_수_있다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        createAnnounceWithDepartment(department);

        // 생성된 공지사항 ID 조회
        Long announceId = getLastAnnounceId();

        UpdateAnnounceRequest request =
                updateAnnounceRequestBuilder()
                        .withTitle("수정된 공지사항 제목")
                        .withContent("수정된 공지사항 내용")
                        .withParticipationDepartmentIds(List.of(department.getId()))
                        .build();

        // when
        updateAnnounce(announceId, request).statusCode(HttpStatus.NO_CONTENT.value());

        // then
        // 수정된 공지사항 조회
        AnnounceCustomPage announceCustomPage =
                getAnnounces(0, 10, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(AnnounceCustomPage.class);

        assertThat(announceCustomPage.content()).hasSize(1);
        assertThat(announceCustomPage.content().getFirst().title()).isEqualTo("수정된 공지사항 제목");
        assertThat(announceCustomPage.content().getFirst().content()).isEqualTo("수정된 공지사항 내용");
    }

    @Test
    void 존재하지_않는_공지사항은_수정할_수_없다() {
        // given
        Long nonExistentAnnounceId = System.currentTimeMillis();
        UpdateAnnounceRequest request = updateAnnounceRequestBuilder().build();

        // when
        ErrorResponse errorResponse =
                updateAnnounce(nonExistentAnnounceId, request)
                        .statusCode(AnnounceErrorCode.ANNOUNCE_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AnnounceErrorCode.ANNOUNCE_NOT_FOUND.getMessage());
    }

    @Test
    void 권한이_없는_사용자는_공지사항을_수정할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        createAnnounceWithDepartment(department);

        // 생성된 공지사항 ID 조회
        Long announceId = getLastAnnounceId();

        // 비조직원으로 로그인
        accessToken = authTestUtil.generateAccessTokenWithAnotherDepartment(Role.MANAGER);

        UpdateAnnounceRequest request = updateAnnounceRequestBuilder().build();

        // when
        ErrorResponse errorResponse =
                updateAnnounce(announceId, request)
                        .statusCode(
                                AnnounceErrorCode.ONLY_MANAGER_CAN_UPDATE_ANNOUNCE
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AnnounceErrorCode.ONLY_MANAGER_CAN_UPDATE_ANNOUNCE.getMessage());
    }

    private void createAnnounce() {
        Department department = organizationTestUtil.createCouncilDepartment();
        createAnnounceWithDepartment(department);
    }

    private void createAnnounceWithDepartment(Department department) {
        CreateAnnounceRequest request =
                createAnnounceRequestBuilder()
                        .withParticipationDepartmentIds(List.of(department.getId()))
                        .build();

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .post(ANNOUNCE_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    public static ValidatableResponse getAnnounces(int page, int size, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("direction", "DESC")
                .queryParam("sort", "createdAt")
                .when()
                .get(ANNOUNCE_URL)
                .then()
                .log()
                .all();
    }

    // 유틸 메서드들
    private int calculateExpectedSize(int totalCount, int pageSize, int page) {
        int startIndex = page * pageSize;
        if (startIndex >= totalCount) {
            return 0; // 페이지가 범위를 벗어나면 빈 리스트
        }
        int remainingItems = totalCount - startIndex;
        return Math.min(remainingItems, pageSize);
    }

    public static ValidatableResponse getMyAnnounces(String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(ANNOUNCE_URL + "/me")
                .then()
                .log()
                .all();
    }

    private Long getLastAnnounceId() {
        return getAnnounces(0, 10, accessToken)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AnnounceCustomPage.class)
                .content()
                .getLast()
                .id();
    }

    private ValidatableResponse deleteAnnounce(Long announceId) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .delete(ANNOUNCE_URL + "/{announce-id}", announceId)
                .then()
                .log()
                .all();
    }

    private ValidatableResponse updateAnnounce(Long announceId, UpdateAnnounceRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .put(ANNOUNCE_URL + "/{announce-id}", announceId)
                .then()
                .log()
                .all();
    }
}
