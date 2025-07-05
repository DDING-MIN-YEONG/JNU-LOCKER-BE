package com.jnulocker.registration.adapter.in;

import static com.jnulocker.events.adapter.in.EventControllerIntegrationTest.getEventLockers;
import static com.jnulocker.events.adapter.in.EventControllerIntegrationTest.getMyEventsWithPageable;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.auth.jwt.exception.JwtErrorCode;
import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.config.RedisTest;
import com.jnulocker.events.application.port.in.response.EventPageable;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.LockerResponse;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.application.port.in.response.MyEventListItem;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.events.exception.EventErrorCode;
import com.jnulocker.events.utils.EventTestUtil;
import com.jnulocker.events.utils.LockerEventContext;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import com.jnulocker.registration.application.port.in.response.RegistrationCustomPage;
import com.jnulocker.registration.application.port.in.response.RegistrationListItem;
import com.jnulocker.registration.application.port.in.response.RegistrationMemberInfo;
import com.jnulocker.registration.application.port.in.response.RegistrationResponse;
import com.jnulocker.registration.exception.RegistrationErrorCode;
import com.jnulocker.registration.utils.RegistrationTestUtil;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.UUID;
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
@DisplayName("이벤트 신청 컨트롤러 통합 테스트")
public class RegistrationControllerIntegrationTest extends RedisTest {

    private static final String REGISTRATION_URL = "/v1/events/{event-id}/registrations";
    private static final String ACCESS_TOKEN = "access_token";

    @LocalServerPort private int port;

    @Autowired private EventTestUtil eventTestUtil;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private AuthTestUtil authTestUtil;

    @Autowired private RegistrationTestUtil registrationTestUtil;

    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        registrationTestUtil.deleteAll();
        memberTestUtil.deleteAll();
        eventTestUtil.deleteAll();
    }

    @Test
    void 사물함_신청을_할_수_있다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // when, then
        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 신청된_사물함_목록을_조회할_수_있다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());

        accessToken = authTestUtil.generateAccessToken(Role.MANAGER);

        // when
        RegistrationCustomPage registrationCustomPage =
                getRegistrations(event.getId())
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(RegistrationCustomPage.class);

        // then
        List<RegistrationListItem> content = registrationCustomPage.content();
        RegistrationListItem listItem = content.getFirst();
        RegistrationMemberInfo memberInfo = listItem.member();

        assertThat(registrationCustomPage.totalElements()).isEqualTo(1);
        assertThat(memberInfo.name()).isEqualTo("테스트 이름");
        assertThat(memberInfo.studentNumber()).isEqualTo("221965");
        assertThat(memberInfo.organization()).isEqualTo("테스트 조직명");
        assertThat(memberInfo.department()).isEqualTo("테스트 학과명");
        assertThat(memberInfo.email()).isEqualTo("test@email.com");
    }

    @Test
    @DisplayName("신청된 사물함 목록 전체 조회 시, 층과 사물함번호 순으로 정렬된다")
    void 신청된_사물함_목록_전체_조회_시_층과_사물함번호_순으로_정렬된다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(10, 5), Role.USER, EventStatus.OPEN, true);
        Event event = context.event();
        Member eventParticipant = context.member();

        // 사용자 2가 2층 사물함을 먼저 신청
        Member user2 =
                memberTestUtil.createMemberFromRoleWithDepartment(
                        Role.USER, eventParticipant.getDepartment());
        String user2Token = authTestUtil.generateAccessTokenWithMember(user2);
        String user2Locker = "B-002";
        RegisterForEventRequest request2 =
                createRequestForSpecificLocker(event, user2Token, 2, user2Locker);
        registerForEvent(event.getId(), request2, user2Token)
                .statusCode(HttpStatus.CREATED.value());

        // 사용자 1이 1층 사물함을 나중에 신청
        Member user1 =
                memberTestUtil.createMemberFromRoleWithDepartment(
                        Role.USER, eventParticipant.getDepartment());
        String user1Token = authTestUtil.generateAccessTokenWithMember(user1);
        String user1Locker = "A-008";
        RegisterForEventRequest request1 =
                createRequestForSpecificLocker(event, user1Token, 1, user1Locker);
        registerForEvent(event.getId(), request1, user1Token)
                .statusCode(HttpStatus.CREATED.value());

        accessToken = authTestUtil.generateAccessToken(Role.MANAGER);

        // when
        List<RegistrationListItem> registrations =
                getAllRegistrations(event.getId())
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .jsonPath()
                        .getList(".", RegistrationListItem.class);

        // then
        assertThat(registrations).hasSize(2);

        // 정렬 순서 검증: 층(asc), 사물함 코드(asc)
        RegistrationListItem firstItem = registrations.get(0);
        RegistrationListItem secondItem = registrations.get(1);

        // 첫 번째 항목 (1층 사물함, 사용자 1)
        assertThat(firstItem.floorNumber()).isEqualTo(1);
        assertThat(firstItem.lockerCode()).isEqualTo(user1Locker);
        assertThat(firstItem.member().name()).isEqualTo(user1.getName());
        assertThat(firstItem.member().studentNumber()).isEqualTo(user1.getStudentNumber());
        assertThat(firstItem.member().organization())
                .isEqualTo(user1.getDepartment().getOrganization().getName());
        assertThat(firstItem.member().department()).isEqualTo(user1.getDepartment().getName());
        assertThat(firstItem.member().email()).isEqualTo(user1.getEmail());

        // 두 번째 항목 (2층 사물함, 사용자 2)
        assertThat(secondItem.floorNumber()).isEqualTo(2);
        assertThat(secondItem.lockerCode()).isEqualTo(user2Locker);
        assertThat(secondItem.member().name()).isEqualTo(user2.getName());
        assertThat(secondItem.member().studentNumber()).isEqualTo(user2.getStudentNumber());
        assertThat(secondItem.member().organization())
                .isEqualTo(user2.getDepartment().getOrganization().getName());
        assertThat(secondItem.member().department()).isEqualTo(user2.getDepartment().getName());
        assertThat(secondItem.member().email()).isEqualTo(user2.getEmail());
    }

    @Test
    void 자신이_신청한_사물함을_조회할_수_있다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());

        RegistrationResponse registrationResponse =
                getMyRegistration(event.getId())
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(RegistrationResponse.class);

        assertThat(registrationResponse.floorNumber()).isEqualTo(1);
    }

    @Test
    void 자신의_사물함_내역이_존재하지_않으면_예외가_발생한다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();

        // when : 신청 없이 신청 내역 조회
        ErrorResponse errorResponse =
                getMyRegistration(event.getId())
                        .statusCode(
                                RegistrationErrorCode.REGISTRATION_NOT_FOUND
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(RegistrationErrorCode.REGISTRATION_NOT_FOUND.getMessage());
    }

    @Test
    void 인증되지_않은_사용자는_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // when
        accessToken = null;
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(JwtErrorCode.INVALID_ACCESS_TOKEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(JwtErrorCode.INVALID_ACCESS_TOKEN.getMessage());
    }

    @Test
    void 사용중인_사물함은_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();

        RegisterForEventRequest request = createRequestForUnavailableLocker(event, accessToken);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(EventErrorCode.LOCKER_UNAVAILABLE.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.LOCKER_UNAVAILABLE.getMessage());
    }

    @Test
    void 동일한_이벤트에_대해_중복으로_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // when
        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());

        // then
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(
                                RegistrationErrorCode.REGISTRATION_ALREADY_EXISTS
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        assertThat(errorResponse.message())
                .isEqualTo(RegistrationErrorCode.REGISTRATION_ALREADY_EXISTS.getMessage());
    }

    @Test
    void 존재하지_않는_이벤트는_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();

        UUID nonExistentEventId = UUID.randomUUID();

        // when
        ErrorResponse errorResponse =
                registerForEvent(
                                nonExistentEventId,
                                createRequestForAvailableLocker(event, accessToken),
                                accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void 이벤트에_해당하지_않는_사물함은_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();

        Event anotherEvent = createEventWithLockers(EventStatus.OPEN, true);
        RegisterForEventRequest request =
                createRequestForAvailableLocker(anotherEvent, accessToken);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(EventErrorCode.INVALID_LOCKER_FOR_EVENT.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.INVALID_LOCKER_FOR_EVENT.getMessage());
    }

    @Test
    void 이벤트가_준비중인_경우_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.READY, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_OPEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_OPEN.getMessage());
    }

    @Test
    void 이벤트가_종료된_경우_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.CLOSED, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_OPEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_OPEN.getMessage());
    }

    @Test
    void USER는_publish_false인_이벤트를_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, false);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_PUBLISHED.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.EVENT_NOT_PUBLISHED.getMessage());
    }

    @Test
    void 소속학과가_아닌_사용자는_신청할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setupLockerEventWithParticipatingDepartment(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true, List.of());

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request, accessToken)
                        .statusCode(
                                EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_REGISTER
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_REGISTER.getMessage());
    }

    @Test
    void 신청한_사물함을_취소할_수_있다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());

        // when, then
        cancelMyRegistration(event.getId()).statusCode(HttpStatus.NO_CONTENT.value());

        // 신청 내역이 삭제되었는지 확인
        ErrorResponse errorResponse =
                getMyRegistration(event.getId())
                        .statusCode(
                                RegistrationErrorCode.REGISTRATION_NOT_FOUND
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        assertThat(errorResponse.message())
                .isEqualTo(RegistrationErrorCode.REGISTRATION_NOT_FOUND.getMessage());
    }

    @Test
    void 취소한_사물함을_다시_신청할_수_있다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());
        cancelMyRegistration(event.getId()).statusCode(HttpStatus.NO_CONTENT.value());

        // when, then
        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 존재하지_않는_이벤트의_사물함_신청을_취소할_수_없다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());

        UUID nonExistentEventId = UUID.randomUUID();

        // when, then
        ErrorResponse errorResponse =
                cancelMyRegistration(nonExistentEventId)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void 신청한_이벤트는_잔여여석이_1개_줄어든다() {
        // given
        LockerEventContext context =
                eventTestUtil.setUpLockerEventForRegistration(
                        List.of(5, 10), Role.USER, EventStatus.OPEN, true);

        accessToken = context.accessToken();

        Event event = context.event();
        RegisterForEventRequest request = createRequestForAvailableLocker(event, accessToken);

        // 신청 전 조회
        EventPageable eventPageable = new EventPageable(0, 10, "DESC", "createdAt");
        MyEventCustomPage myEventsBeforeRegistration =
                getMyEventsWithPageable(accessToken, eventPageable)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventCustomPage.class);

        Long availableLockerCountBefore =
                myEventsBeforeRegistration.content().getFirst().availableLockerCount();

        // 신청
        registerForEvent(event.getId(), request, accessToken)
                .statusCode(HttpStatus.CREATED.value());

        // when
        MyEventCustomPage myEventsAfterRegistration =
                getMyEventsWithPageable(accessToken, eventPageable)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventCustomPage.class);

        MyEventListItem eventAfterRegistration = myEventsAfterRegistration.content().getFirst();

        // then
        assertThat(myEventsAfterRegistration.content()).hasSize(1);
        assertThat(eventAfterRegistration.availableLockerCount())
                .isEqualTo(availableLockerCountBefore - 1);
    }

    private ValidatableResponse getRegistrations(UUID eventId) {
        return given().cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(REGISTRATION_URL, eventId)
                .then()
                .log()
                .ifError();
    }

    private ValidatableResponse getAllRegistrations(UUID eventId) {
        return given().cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(REGISTRATION_URL + "/all", eventId)
                .then()
                .log()
                .ifError();
    }

    private ValidatableResponse getMyRegistration(UUID eventId) {
        return given().cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(REGISTRATION_URL + "/me", eventId)
                .then()
                .log()
                .ifError();
    }

    public static ValidatableResponse registerForEvent(
            UUID eventId, RegisterForEventRequest request, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .post(REGISTRATION_URL, eventId)
                .then()
                .log()
                .ifError();
    }

    private ValidatableResponse cancelMyRegistration(UUID eventId) {
        return given().cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .delete(REGISTRATION_URL + "/me", eventId)
                .then()
                .log()
                .ifError();
    }

    private Event createEventWithLockers(EventStatus status, boolean publish) {
        return eventTestUtil.createEventWithFloorAndLockers(List.of(5, 10), status, publish);
    }

    public static RegisterForEventRequest createRequestForAvailableLocker(
            Event event, String accessToken) {
        List<FloorWithLockersResponse> floors = getFloors(event, accessToken);
        // 짝수 번째 인덱스의 사물함은 테스트에서 사용 가능한 상태 (eventTestUtil 참고)
        UUID lockerId = null;
        for (FloorWithLockersResponse floor : floors) {
            for (var locker : floor.lockers()) {
                if (locker.available()) {
                    lockerId = locker.lockerId();
                    break;
                }
            }
            if (lockerId != null) {
                break;
            }
        }
        return new RegisterForEventRequest(lockerId);
    }

    private RegisterForEventRequest createRequestForUnavailableLocker(
            Event event, String accessToken) {
        List<FloorWithLockersResponse> floors = getFloors(event, accessToken);
        // 홀수 번째 인덱스의 사물함은 테스트에서 사용 불가능한 상태 (eventTestUtil 참고)
        UUID lockerId = null;
        for (FloorWithLockersResponse floor : floors) {
            for (var locker : floor.lockers()) {
                if (!locker.available()) {
                    lockerId = locker.lockerId();
                    break;
                }
            }
            if (lockerId != null) {
                break;
            }
        }
        return new RegisterForEventRequest(lockerId);
    }

    private static List<FloorWithLockersResponse> getFloors(Event event, String accessToken) {
        return getEventLockers(event.getId(), accessToken)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", FloorWithLockersResponse.class);
    }

    private RegisterForEventRequest createRequestForSpecificLocker(
            Event event, String accessToken, int floorNumber, String lockerNumber) {
        List<FloorWithLockersResponse> floors = getFloors(event, accessToken);
        UUID lockerId =
                floors.stream()
                        .filter(floor -> floor.floorNumber() == floorNumber)
                        .flatMap(floor -> floor.lockers().stream())
                        .filter(locker -> locker.code().equals(lockerNumber))
                        .findFirst()
                        .map(LockerResponse::lockerId)
                        .orElseThrow(() -> new AssertionError("Cannot find specified locker"));
        return new RegisterForEventRequest(lockerId);
    }
}
