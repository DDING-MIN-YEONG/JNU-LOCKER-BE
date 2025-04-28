package com.jnulocker.registration.adapter.in;

import static com.jnulocker.events.adapter.in.EventControllerIntegrationTest.getEventLockers;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.auth.jwt.TokenProvider;
import com.jnulocker.auth.jwt.exception.JwtErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.events.exception.EventErrorCode;
import com.jnulocker.events.utils.EventTestUtil;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.registration.application.port.in.request.RegisterForEventRequest;
import com.jnulocker.registration.exception.RegistrationErrorCode;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ValidatableResponse;
import java.util.List;
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
class RegistrationControllerIntegrationTest {

    private static final String REGISTRATION_URL = "/v1/{event-id}/registrations";
    private static final String ACCESS_TOKEN = "access_token";

    @LocalServerPort private int port;

    @Autowired private EventTestUtil eventTestUtil;

    @Autowired private TokenProvider tokenProvider;

    @Autowired private MemberTestUtil memberTestUtil;

    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        accessToken = generateAccessToken(Role.USER);
    }

    @Test
    void 사물함_신청을_할_수_있다() {
        // given
        Event event = createEventWithLockers(EventStatus.OPEN, true);
        RegisterForEventRequest request = createRequestForAvailableLocker(event);

        // when, then
        registerForEvent(event.getId(), request).statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 인증되지_않은_사용자는_신청할_수_없다() {
        // given
        Event event = createEventWithLockers(EventStatus.OPEN, true);
        RegisterForEventRequest request = createRequestForAvailableLocker(event);

        // when
        ErrorResponse errorResponse =
                registerForEventWithoutLoggedUser(event.getId(), request)
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
        Event event = createEventWithLockers(EventStatus.OPEN, true);
        RegisterForEventRequest request = createRequestForUnavailableLocker(event);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request)
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
        Event event = createEventWithLockers(EventStatus.OPEN, true);
        RegisterForEventRequest request = createRequestForAvailableLocker(event);

        // when
        registerForEvent(event.getId(), request).statusCode(HttpStatus.CREATED.value());

        // then
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request)
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
        Event event = createEventWithLockers(EventStatus.OPEN, true);

        // when
        ErrorResponse errorResponse =
                registerForEvent(Long.MAX_VALUE, createRequestForAvailableLocker(event))
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void 이벤트에_해당하지_않는_사물함은_신청할_수_없다() {
        // given
        Event event = createEventWithLockers(EventStatus.OPEN, true);
        Event anotherEvent = createEventWithLockers(EventStatus.OPEN, true);
        RegisterForEventRequest request = createRequestForAvailableLocker(anotherEvent);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request)
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
        Event event = createEventWithLockers(EventStatus.READY, true);
        RegisterForEventRequest request = createRequestForAvailableLocker(event);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request)
                        .statusCode(EventErrorCode.EVENT_NOT_OPEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_OPEN.getMessage());
    }

    @Test
    void 이벤트가_종료된_경우_신청할_수_없다() {
        // given
        Event event = createEventWithLockers(EventStatus.CLOSED, true);
        RegisterForEventRequest request = createRequestForAvailableLocker(event);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request)
                        .statusCode(EventErrorCode.EVENT_NOT_OPEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_OPEN.getMessage());
    }

    @Test
    void USER는_publish_false인_이벤트를_신청할_수_없다() {
        // given
        Event event = createEventWithLockers(EventStatus.OPEN, false);
        RegisterForEventRequest request = createRequestForAvailableLocker(event);

        // when
        ErrorResponse errorResponse =
                registerForEvent(event.getId(), request)
                        .statusCode(EventErrorCode.EVENT_NOT_OPEN.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_OPEN.getMessage());
    }

    @Test
    void MANAGER는_publish_false인_이벤트도_신청할_수_있다() {
        // given
        accessToken = generateAccessToken(Role.MANAGER);
        Event event = createEventWithLockers(EventStatus.OPEN, false);
        RegisterForEventRequest request = createRequestForAvailableLocker(event);

        // when, then
        registerForEvent(event.getId(), request).statusCode(HttpStatus.CREATED.value());
    }

    private ValidatableResponse registerForEvent(Long eventId, RegisterForEventRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .post(REGISTRATION_URL, eventId)
                .then()
                .log()
                .ifError();
    }

    private ValidatableResponse registerForEventWithoutLoggedUser(
            Long eventId, RegisterForEventRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(REGISTRATION_URL, eventId)
                .then()
                .log()
                .ifError();
    }

    private Event createEventWithLockers(EventStatus status, boolean publish) {
        return eventTestUtil.createEventWithFloorAndLockers(List.of(5, 10), status, publish);
    }

    private String generateAccessToken(Role role) {
        Member member =
                role == Role.MANAGER ? memberTestUtil.createManager() : memberTestUtil.createUser();
        return tokenProvider.generateAccessToken(member.getId(), role);
    }

    private RegisterForEventRequest createRequestForAvailableLocker(Event event) {
        List<FloorWithLockersResponse> floors = getFloors(event);
        // 짝수 번째 인덱스의 사물함은 테스트에서 사용 가능한 상태 (eventTestUtil 참고)
        Long lockerId = floors.getFirst().lockers().get(1).lockerId(); // 짝수 번째 사용 가능
        return new RegisterForEventRequest(lockerId);
    }

    private RegisterForEventRequest createRequestForUnavailableLocker(Event event) {
        List<FloorWithLockersResponse> floors = getFloors(event);
        // 홀수 번째 인덱스의 사물함은 테스트에서 사용 불가능한 상태 (eventTestUtil 참고)
        Long lockerId = floors.getLast().lockers().getFirst().lockerId(); // 홀수 번째 사용 불가
        return new RegisterForEventRequest(lockerId);
    }

    private List<FloorWithLockersResponse> getFloors(Event event) {
        return getEventLockers(event.getId(), accessToken)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", FloorWithLockersResponse.class);
    }
}
