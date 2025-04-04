package com.jnulocker.events.adapter.in;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.events.adapter.out.EventRepository;
import com.jnulocker.events.adapter.out.FloorRepository;
import com.jnulocker.events.adapter.out.LockerRepository;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.exception.EventErrorCode;
import com.jnulocker.events.utils.EventTestUtil;
import com.jnulocker.organization.adapter.out.OrganizationRepository;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
class EventControllerIntegrationTest {

    private static final String EVENT_URL = "/v1/events";

    @LocalServerPort private int port;

    @Autowired private OrganizationRepository organizationRepository;

    @Autowired private EventRepository eventRepository;

    @Autowired private FloorRepository floorRepository;

    @Autowired private LockerRepository lockerRepository;

    @Autowired private EventTestUtil eventTestUtil;

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
        lockerRepository.deleteAll();
        floorRepository.deleteAll();
        eventRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @ParameterizedTest(name = "층별 사물함 수: {0}")
    @MethodSource("provideLockersPerFloor")
    void 이벤트에_해당하는_층과_사물함_목록을_조회할_수_있다(List<Integer> lockersPerFloor) {
        // given
        // 이벤트 데이터 생성: 층별 사물함 수를 파라미터로 받아 생성
        Event event = eventTestUtil.createEventWithFloorAndLockers(lockersPerFloor);

        // when
        List<FloorWithLockersResponse> floors =
                getEventLockers(port, event.getId())
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
                getEventLockers(port, nonExistentEventId)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    public static ValidatableResponse getEventLockers(int port, Long eventId) {
        return given().port(port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(EVENT_URL + "/{event-id}/lockers", eventId)
                .then()
                .log()
                .all();
    }
}
