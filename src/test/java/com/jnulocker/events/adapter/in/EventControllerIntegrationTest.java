package com.jnulocker.events.adapter.in;

import static events.application.port.in.request.CreateEventRequestTestDataBuilder.createEventRequestBuilder;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.events.exception.EventErrorCode;
import com.jnulocker.events.utils.EventTestUtil;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.utils.OrganizationUtil;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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
@DisplayName("이벤트 컨트롤러 통합 테스트")
public class EventControllerIntegrationTest {

    private static final String EVENT_URL = "/v1/events";
    private static final String ACCESS_TOKEN = "access_token";

    @LocalServerPort private int port;

    @Autowired private EventTestUtil eventTestUtil;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private AuthTestUtil authTestUtil;

    @Autowired private OrganizationUtil organizationUtil;

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
        eventTestUtil.deleteAll();
    }

    // 이벤트 목록 조회 테스트
    @ParameterizedTest(name = "[{index}] 조회[총: {0}, 크기: {1}, 페이지: {2}] -> 마지막: {3}")
    @CsvSource({
        "10, 5, 0, false", // 10개 생성, 5개씩, 0페이지(1~5), 마지막 아님
        "10, 5, 1, true", // 10개 생성, 5개씩, 1페이지(6~10), 마지막
        "8, 3, 0, false", // 8개 생성, 3개씩, 0페이지(1~3), 마지막 아님
        "8, 3, 2, true", // 8개 생성, 3개씩, 2페이지(7~8), 마지막
        "5, 10, 0, true" // 5개 생성, 10개씩, 0페이지(1~5), 마지막
    })
    void 이벤트_목록을_조회할_수_있다(int createCount, int pageSize, int page, boolean expectedLast) {
        // given
        for (int i = 0; i < createCount; i++) {
            eventTestUtil.createEventWithFloorAndLockers(List.of(1), EventStatus.OPEN, true);
        }

        // when
        EventCustomPage eventCustomPage =
                getEvents(page, pageSize, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(EventCustomPage.class);

        // then
        int expectedSize = calculateExpectedSize(createCount, pageSize, page);
        assertThat(eventCustomPage.content()).hasSize(expectedSize);
        assertThat(eventCustomPage.totalElements()).isEqualTo(createCount);
        assertThat(eventCustomPage.last()).isEqualTo(expectedLast);
    }

    private int calculateExpectedSize(int totalCount, int pageSize, int page) {
        int startIndex = page * pageSize;
        if (startIndex >= totalCount) {
            return 0; // 페이지가 범위를 벗어나면 빈 리스트
        }
        int remainingItems = totalCount - startIndex;
        return Math.min(remainingItems, pageSize);
    }

    public static ValidatableResponse getEvents(int page, int size, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("direction", "DESC")
                .queryParam("sort", "createdAt")
                .when()
                .get(EVENT_URL)
                .then()
                .log()
                .all();
    }

    @ParameterizedTest(name = "층별 사물함 수: {0}")
    @MethodSource("provideLockersPerFloor")
    void 이벤트에_해당하는_층과_사물함_목록을_조회할_수_있다(List<Integer> lockersPerFloor) {
        // given
        // 이벤트 데이터 생성: 층별 사물함 수를 파라미터로 받아 생성
        Event event =
                eventTestUtil.createEventWithFloorAndLockers(
                        lockersPerFloor, EventStatus.OPEN, true);

        // when
        List<FloorWithLockersResponse> floors =
                getEventLockers(event.getId(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath()
                        .getList(".", FloorWithLockersResponse.class);

        // then
        // 층 개수 검증
        assertThat(floors).hasSize(lockersPerFloor.size());

        // 각 층의 사물함 개수 검증
        assertThat(floors)
                .extracting(FloorWithLockersResponse::lockers)
                .map(List::size)
                .containsExactlyElementsOf(lockersPerFloor);
    }

    // 파라미터 제공 메서드
    private static Stream<Arguments> provideLockersPerFloor() {
        return Stream.of(
                Arguments.of(List.of(2, 2)), // 첫 번째 테스트 케이스: 층별 2개 사물함
                Arguments.of(List.of(3, 5)) // 두 번째 테스트 케이스: 1층 3개, 2층 5개
                );
    }

    @Test
    void 존재하지_않는_이벤트_ID로_조회하면_Event_Not_Found_에러_응답을_받는다() {
        // given
        Long nonExistentEventId = -1L;

        // when
        ErrorResponse errorResponse =
                getEventLockers(nonExistentEventId, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    public static ValidatableResponse getEventLockers(Long eventId, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(EVENT_URL + "/{event-id}/lockers", eventId)
                .then()
                .log()
                .all();
    }

    @Test
    void 이벤트를_생성할_수_있다() {
        // given
        createEvent();

        // then
        // 생성된 이벤트 조회
        EventCustomPage eventCustomPage =
                getEvents(0, 10, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(EventCustomPage.class);

        // 생성된 이벤트가 목록에 포함되어 있는지 확인
        assertThat(eventCustomPage.content()).hasSize(1);
    }

    private void createEvent() {
        Department department = organizationUtil.createDepartment();

        CreateEventRequest request =
                createEventRequestBuilder()
                        .withParticipationDepartmentIds(List.of(department.getId()))
                        .build();

        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .post(EVENT_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 이벤트를_삭제할_수_있다() {
        // given
        createEvent();

        // 생성된 이벤트 조회
        Long eventId = getLastEventId();

        // when
        deleteEvent(eventId).statusCode(HttpStatus.NO_CONTENT.value());

        // then
        // 삭제된 이벤트 조회 시 예외 발생
        ErrorResponse errorResponse =
                getEventLockers(eventId, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    private Long getLastEventId() {
        return getEvents(0, 10, accessToken)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(EventCustomPage.class)
                .content()
                .getLast()
                .id();
    }

    private ValidatableResponse deleteEvent(Long eventId) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .delete(EVENT_URL + "/{event-id}", eventId)
                .then()
                .log()
                .all();
    }

    @Test
    void 존재하지_않는_이벤트는_삭제할_수_없다() {
        // given
        Long nonExistentEventId = Long.MAX_VALUE;

        // when
        ErrorResponse errorResponse =
                deleteEvent(nonExistentEventId)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void 권한이_없는_사용자는_이벤트를_삭제할_수_없다() {
        // given
        createEvent();

        // 생성된 이벤트 조회
        Long eventId = getLastEventId();

        // 비조직원으로 로그인
        accessToken = authTestUtil.generateAccessTokenWithAnotherDepartment(Role.MANAGER);

        // when
        ErrorResponse errorResponse =
                deleteEvent(eventId)
                        .statusCode(EventErrorCode.ONLY_MANAGER_CAN_DELETE.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_MANAGER_CAN_DELETE.getMessage());
    }
}
