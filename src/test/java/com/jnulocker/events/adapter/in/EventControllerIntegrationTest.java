package com.jnulocker.events.adapter.in;

import static events.application.port.in.request.CreateEventRequestTestDataBuilder.createEventRequestBuilder;
import static events.application.port.in.request.UpdateEventRequestTestDataBuilder.updateEventRequestBuilder;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.events.application.port.in.request.CreateEventRequest;
import com.jnulocker.events.application.port.in.request.FloorInfo;
import com.jnulocker.events.application.port.in.request.LockerRange;
import com.jnulocker.events.application.port.in.request.PrefixInfo;
import com.jnulocker.events.application.port.in.request.PublishEventRequest;
import com.jnulocker.events.application.port.in.request.UpdateEventRequest;
import com.jnulocker.events.application.port.in.response.EventCustomPage;
import com.jnulocker.events.application.port.in.response.EventDepartmentResponse;
import com.jnulocker.events.application.port.in.response.EventListItem;
import com.jnulocker.events.application.port.in.response.EventPageable;
import com.jnulocker.events.application.port.in.response.EventResponse;
import com.jnulocker.events.application.port.in.response.FloorWithLockersResponse;
import com.jnulocker.events.application.port.in.response.MyEventCustomPage;
import com.jnulocker.events.application.port.in.response.MyEventResponse;
import com.jnulocker.events.domain.Event;
import com.jnulocker.events.domain.EventStatus;
import com.jnulocker.events.exception.EventErrorCode;
import com.jnulocker.events.utils.EventTestUtil;
import com.jnulocker.member.domain.Member;
import com.jnulocker.member.domain.Role;
import com.jnulocker.member.utils.MemberTestUtil;
import com.jnulocker.organization.domain.Department;
import com.jnulocker.organization.utils.OrganizationTestUtil;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ValidatableResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
    private static final String DIRECTION = "DESC";
    private static final String SORT = "createdAt";

    @LocalServerPort private int port;

    @Autowired private EventTestUtil eventTestUtil;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private AuthTestUtil authTestUtil;

    @Autowired private OrganizationTestUtil organizationTestUtil;

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
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        for (int i = 0; i < createCount; i++) {
            eventTestUtil.createEventWithParticipationDepartment(
                    List.of(5, 10), department, EventStatus.OPEN, true);
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

    @Test
    void 자신의_소속학과가_주관하지_않는_이벤트는_조회할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // 이벤트 생성: 비참여 학과로 생성
        eventTestUtil.createEventWithFloorAndLockers(List.of(5, 10), EventStatus.OPEN, true);

        // when
        accessToken = authTestUtil.generateAccessToken(Role.MANAGER);
        EventCustomPage events =
                getEvents(0, 10, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(EventCustomPage.class);

        // then
        assertThat(events.content()).isEmpty();
        assertThat(events.totalElements()).isZero();
        assertThat(events.last()).isTrue();
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
                .containsAnyElementsOf(lockersPerFloor);
    }

    @Test
    void 존재하지_않는_이벤트_ID로_조회하면_Event_Not_Found_에러_응답을_받는다() {
        // given
        UUID nonExistentEventId = UUID.randomUUID();

        // when
        ErrorResponse errorResponse =
                getEventLockers(nonExistentEventId, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
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

    @Test
    void 이벤트를_삭제할_수_있다() {
        // given
        createEvent();

        // 생성된 이벤트 조회
        UUID eventId = getFirstEventId();

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

    @Test
    void 존재하지_않는_이벤트는_삭제할_수_없다() {
        // given
        UUID nonExistentEventId = UUID.randomUUID();

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
        UUID eventId = getFirstEventId();

        // 비조직원으로 로그인
        accessToken = authTestUtil.generateAccessTokenWithAnotherDepartment(Role.MANAGER);

        // when
        ErrorResponse errorResponse =
                deleteEvent(eventId)
                        .statusCode(
                                EventErrorCode.ONLY_MANAGER_CAN_DELETE_EVENT
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_MANAGER_CAN_DELETE_EVENT.getMessage());
    }

    @Test
    void 생성된_이벤트는_publish_상태로_변경할_수_있다() {
        // given
        createEvent();

        // 생성된 이벤트 조회
        UUID eventId = getFirstEventId();

        PublishEventRequest request = new PublishEventRequest(true);

        // when
        publishEvent(eventId, request).statusCode(HttpStatus.NO_CONTENT.value());

        // then
        Event event = eventTestUtil.getEventById(eventId);
        assertThat(event.getPublish()).isTrue();
    }

    @Test
    void 생성된_이벤트는_unpublish_상태로_변경할_수_있다() {
        // given
        createEvent();

        // 생성된 이벤트 조회
        UUID eventId = getFirstEventId();

        PublishEventRequest request = new PublishEventRequest(false);

        // when
        publishEvent(eventId, request).statusCode(HttpStatus.NO_CONTENT.value());

        // then
        Event event = eventTestUtil.getEventById(eventId);
        assertThat(event.getPublish()).isFalse();
    }

    @Test
    void 이벤트를_수정할_수_있다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // 이벤트 생성
        createEventWithDepartment(department);

        UUID eventId = getFirstEventId();

        // 수정 요청 데이터 생성
        String updateTitle = "수정된 이벤트";
        UpdateEventRequest updateRequest =
                updateEventRequestBuilder()
                        .withTitle(updateTitle)
                        .withParticipationDepartmentIds(List.of(department.getId()))
                        .build();

        // when
        updateEvent(eventId, updateRequest).statusCode(HttpStatus.NO_CONTENT.value());

        // then
        EventResponse eventResponse =
                getEventDetail(eventId, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(EventResponse.class);

        assertThat(eventResponse.title()).isEqualTo(updateTitle);
        assertThat(eventResponse.startAt()).isEqualTo(updateRequest.startAt());
        assertThat(eventResponse.endAt()).isEqualTo(updateRequest.endAt());
    }

    @Test
    void 존재하지_않는_이벤트는_수정할_수_없다() {
        // given
        UUID nonExistentEventId = UUID.randomUUID();

        UpdateEventRequest updateRequest = updateEventRequestBuilder().build();

        // when
        ErrorResponse errorResponse =
                updateEvent(nonExistentEventId, updateRequest)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void 권한이_없는_사용자는_이벤트를_수정할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // 이벤트 생성
        createEventWithDepartment(department);

        UUID eventId = getFirstEventId();

        // 다른 부서의 사용자로 로그인
        accessToken = authTestUtil.generateAccessTokenWithAnotherDepartment(Role.MANAGER);

        // 수정 요청 데이터 생성
        UpdateEventRequest updateRequest = updateEventRequestBuilder().build();

        // when
        ErrorResponse errorResponse =
                updateEvent(eventId, updateRequest)
                        .statusCode(
                                EventErrorCode.ONLY_MANAGER_CAN_UPDATE_EVENT
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_MANAGER_CAN_UPDATE_EVENT.getMessage());
    }

    @Test
    void 진행되는_이벤트는_수정할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // 이벤트 생성 (OPEN 상태)
        Event event =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5, 10), department, EventStatus.OPEN, true);

        // 수정 요청 데이터 생성
        UpdateEventRequest updateRequest = updateEventRequestBuilder().build();

        // when
        ErrorResponse errorResponse =
                updateEvent(event.getId(), updateRequest)
                        .statusCode(
                                EventErrorCode.ONLY_READY_EVENT_CAN_BE_UPDATED
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_READY_EVENT_CAN_BE_UPDATED.getMessage());
    }

    @Test
    void 종료된_이벤트는_수정할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // 이벤트 생성 (CLOSED 상태)
        Event event =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5, 10), department, EventStatus.CLOSED, true);

        // 수정 요청 데이터 생성
        UpdateEventRequest updateRequest = updateEventRequestBuilder().build();

        // when
        ErrorResponse errorResponse =
                updateEvent(event.getId(), updateRequest)
                        .statusCode(
                                EventErrorCode.ONLY_READY_EVENT_CAN_BE_UPDATED
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_READY_EVENT_CAN_BE_UPDATED.getMessage());
    }

    @Test
    void 존재하지_않는_이벤트는_publish_상태로_변경할_수_없다() {
        // given
        UUID nonExistentEventId = UUID.randomUUID();

        PublishEventRequest request = new PublishEventRequest(true);

        // when
        ErrorResponse errorResponse =
                publishEvent(nonExistentEventId, request)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void 자신이_속한_학과가_참여하는_이벤트를_조회할_수_있다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);

        // 이벤트 생성: 짝수번 사물함은 사용 가능, 홀수번 사물함은 사용 불가능
        // 15개 중 가능한 사물함 수는 2, 4, 6, 8, 10, 12, 14 (총 7개)
        eventTestUtil.createEventWithParticipationDepartment(
                List.of(5, 10), department, EventStatus.OPEN, true);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        EventPageable eventPageable = new EventPageable(0, 10, DIRECTION, SORT);
        MyEventCustomPage myEvents =
                getMyEventsWithPageable(accessToken, eventPageable)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventCustomPage.class);

        // then
        // 자신의 소속 학과가 참여하는 이벤트가 조회되었는지 확인
        assertThat(myEvents.content()).hasSize(1);
        assertThat(myEvents.totalElements()).isEqualTo(1);
        assertThat(myEvents.last()).isTrue();
        assertThat(myEvents.content().getFirst().availableLockerCount()).isEqualTo(7);
    }

    @Test
    void publish되지_않은_이벤트는_조회할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);

        // 이벤트 생성: 짝수번 사물함은 사용 가능, 홀수번 사물함은 사용 불가능
        // 15개 중 가능한 사물함 수는 2, 4, 6, 8, 10, 12, 14 (총 7개)
        eventTestUtil.createEventWithParticipationDepartment(
                List.of(5, 10), department, EventStatus.OPEN, false);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        EventPageable eventPageable = new EventPageable(0, 10, DIRECTION, SORT);
        MyEventCustomPage myEvents =
                getMyEventsWithPageable(accessToken, eventPageable)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventCustomPage.class);

        // then
        assertThat(myEvents.content()).isEmpty();
        assertThat(myEvents.totalElements()).isZero();
        assertThat(myEvents.last()).isTrue();
    }

    @Test
    void 이벤트_상세조회가_가능하다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);

        // 이벤트 생성: 짝수번 사물함은 사용 가능, 홀수번 사물함은 사용 불가능
        // 15개 중 가능한 사물함 수는 2, 4, 6, 8, 10, 12, 14 (총 7개)
        eventTestUtil.createEventWithParticipationDepartment(
                List.of(5, 10), department, EventStatus.OPEN, true);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        EventCustomPage eventCustomPage =
                getEvents(0, 10, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(EventCustomPage.class);

        EventListItem myDepartmentEvent = eventCustomPage.content().getFirst();
        EventResponse eventResponse =
                getEventDetail(myDepartmentEvent.id(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(EventResponse.class);

        // then
        // 자신의 소속 학과가 참여하는 이벤트가 조회되었는지 확인
        assertThat(eventResponse.status()).isEqualTo(EventStatus.OPEN);
        assertThat(eventResponse.participationDepartments())
                .containsExactly(
                        new EventDepartmentResponse(department.getId(), department.getName()));
        assertThat(eventResponse.startAt()).isEqualTo(myDepartmentEvent.startAt());
        assertThat(eventResponse.endAt()).isEqualTo(myDepartmentEvent.endAt());
        assertThat(eventResponse.title()).isEqualTo(myDepartmentEvent.title());
        assertThat(eventResponse.publish()).isTrue();

        // 층 정보 검증
        assertThat(eventResponse.floors()).isNotNull();
        assertThat(eventResponse.floors()).isNotEmpty();

        // 층 내 사물함 정보 검증
        eventResponse
                .floors()
                .forEach(
                        floor -> {
                            assertThat(floor.floorNumber()).isNotNull();
                            assertThat(floor.prefixes()).isNotEmpty();

                            floor.prefixes()
                                    .forEach(
                                            prefix -> {
                                                assertThat(prefix.ranges()).isNotEmpty();

                                                prefix.ranges()
                                                        .forEach(
                                                                range -> {
                                                                    assertThat(
                                                                                    range
                                                                                            .lockerStartNumber())
                                                                            .isNotNull();
                                                                    assertThat(
                                                                                    range
                                                                                            .lockerEndNumber())
                                                                            .isNotNull();
                                                                    assertThat(
                                                                                    range
                                                                                            .lockerEndNumber())
                                                                            .isGreaterThanOrEqualTo(
                                                                                    range
                                                                                            .lockerStartNumber());
                                                                });
                                            });
                        });
    }

    @Test
    void 존재하지_않는_이벤트는_상세조회가_불가능하다() {
        // given
        UUID nonExistentEventId = UUID.randomUUID();

        // when
        ErrorResponse errorResponse =
                getEventDetail(nonExistentEventId, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void 자신이_속한_학과가_참여하는_이벤트의_상세정보를_조회할_수_있다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);

        // 이벤트 생성: 해당 학과가 참여하는 이벤트
        Event event =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5, 10), department, EventStatus.OPEN, true);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        MyEventResponse myEventResponse =
                getMyEventDetail(event.getId(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventResponse.class);

        // then
        assertThat(myEventResponse.id()).isEqualTo(event.getId());
        assertThat(myEventResponse.title()).isEqualTo(event.getTitle());
        assertThat(myEventResponse.startAt()).isEqualTo(event.getEventSchedule().getStartAt());
        assertThat(myEventResponse.endAt()).isEqualTo(event.getEventSchedule().getEndAt());
        assertThat(myEventResponse.status()).isEqualTo(event.getEventStatus());
    }

    @Test
    void 자신이_속한_학과가_참여하지_않는_이벤트는_상세조회할_수_없다() {
        // given
        Department participatingDepartment = organizationTestUtil.createCouncilDepartment();
        Department nonParticipatingDepartment = organizationTestUtil.createCommitteeDepartment();
        Member member =
                memberTestUtil.createMemberFromRoleWithDepartment(
                        Role.USER, nonParticipatingDepartment);

        // 이벤트 생성: 다른 학과만 참여하는 이벤트
        Event event =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5, 10), participatingDepartment, EventStatus.OPEN, true);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        ErrorResponse errorResponse =
                getMyEventDetail(event.getId(), accessToken)
                        .statusCode(
                                EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_SEE_EVENT
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_SEE_EVENT.getMessage());
    }

    @Test
    void 존재하지_않는_이벤트에_대한_내_이벤트_상세조회는_불가능하다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);
        UUID nonExistentEventId = UUID.randomUUID();

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        ErrorResponse errorResponse =
                getMyEventDetail(nonExistentEventId, accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(EventErrorCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    void publish되지_않은_이벤트는_내_이벤트_상세조회할_수_없다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);

        // 이벤트 생성: publish = false
        Event event =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5, 10), department, EventStatus.OPEN, false);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        ErrorResponse errorResponse =
                getMyEventDetail(event.getId(), accessToken)
                        .statusCode(EventErrorCode.EVENT_NOT_PUBLISHED.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.EVENT_NOT_PUBLISHED.getMessage());
    }

    @Test
    void publish된_이벤트만_내_이벤트_상세조회가_가능하다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);

        // 이벤트 생성: publish = true
        Event publishedEvent =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5, 10), department, EventStatus.OPEN, true);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        MyEventResponse myEventResponse =
                getMyEventDetail(publishedEvent.getId(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventResponse.class);

        // then
        assertThat(myEventResponse.id()).isEqualTo(publishedEvent.getId());
        assertThat(myEventResponse.title()).isEqualTo(publishedEvent.getTitle());
        assertThat(myEventResponse.status()).isEqualTo(publishedEvent.getEventStatus());
    }

    @Test
    void 참여하지_않는_학과_소속이면서_publish되지_않은_이벤트는_권한_오류가_먼저_발생한다() {
        // given
        Department participatingDepartment = organizationTestUtil.createCouncilDepartment();
        Department nonParticipatingDepartment = organizationTestUtil.createCommitteeDepartment();
        Member member =
                memberTestUtil.createMemberFromRoleWithDepartment(
                        Role.USER, nonParticipatingDepartment);

        // 이벤트 생성: 다른 학과만 참여, publish = false
        Event event =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5, 10), participatingDepartment, EventStatus.OPEN, false);

        // when
        accessToken = authTestUtil.generateAccessTokenWithMember(member);
        ErrorResponse errorResponse =
                getMyEventDetail(event.getId(), accessToken)
                        .statusCode(
                                EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_SEE_EVENT
                                        .getHttpStatus()
                                        .value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        // 권한 검증이 publish 검증보다 우선되어 권한 오류가 발생
        assertThat(errorResponse.message())
                .isEqualTo(EventErrorCode.ONLY_PARTICIPATION_DEPARTMENT_CAN_SEE_EVENT.getMessage());
    }

    @Test
    void 다양한_이벤트_상태의_내_이벤트_상세조회가_가능하다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.USER, department);

        // 다양한 상태의 이벤트 생성
        Event readyEvent =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5), department, EventStatus.READY, true);
        Event openEvent =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5), department, EventStatus.OPEN, true);
        Event closedEvent =
                eventTestUtil.createEventWithParticipationDepartment(
                        List.of(5), department, EventStatus.CLOSED, true);

        // when & then
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // READY 상태 이벤트 조회
        MyEventResponse readyEventResponse =
                getMyEventDetail(readyEvent.getId(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventResponse.class);
        assertThat(readyEventResponse.status()).isEqualTo(EventStatus.READY);

        // OPEN 상태 이벤트 조회
        MyEventResponse openEventResponse =
                getMyEventDetail(openEvent.getId(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventResponse.class);
        assertThat(openEventResponse.status()).isEqualTo(EventStatus.OPEN);

        // CLOSED 상태 이벤트 조회
        MyEventResponse closedEventResponse =
                getMyEventDetail(closedEvent.getId(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(MyEventResponse.class);
        assertThat(closedEventResponse.status()).isEqualTo(EventStatus.CLOSED);
    }

    @Test
    void 다양한_층과_접두사_범위로_생성된_이벤트_상세조회시_정확히_반환된다() {
        // given
        Department department = organizationTestUtil.createCouncilDepartment();
        Member member = memberTestUtil.createMemberFromRoleWithDepartment(Role.MANAGER, department);
        accessToken = authTestUtil.generateAccessTokenWithMember(member);

        // 다양한 층과 접두사, 범위를 가진 이벤트 생성 요청 준비
        CreateEventRequest request = prepareComplexEventRequest(department.getId());

        // 이벤트 생성
        createCustomEvent(request);
        UUID eventId = getFirstEventId();

        // when
        EventResponse eventResponse =
                getEventDetail(eventId, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(EventResponse.class);

        // then
        // 기본 정보 검증
        assertThat(eventResponse.title()).isEqualTo(request.title());

        // 층 개수가 일치하는지 검증
        assertThat(eventResponse.floors()).hasSameSizeAs(request.floors());

        // 층 정보 비교를 위한 정렬
        List<FloorInfo> requestFloors = new ArrayList<>(request.floors());
        List<FloorInfo> responseFloors = new ArrayList<>(eventResponse.floors());
        requestFloors.sort(Comparator.comparing(FloorInfo::floorNumber));
        responseFloors.sort(Comparator.comparing(FloorInfo::floorNumber));

        // 각 층별로 세부 검증
        for (int i = 0; i < requestFloors.size(); i++) {
            FloorInfo requestFloor = requestFloors.get(i);
            FloorInfo responseFloor = responseFloors.get(i);
            int floorNumber = requestFloor.floorNumber();

            // 2.1 층 번호 일치 검증
            assertThat(responseFloor.floorNumber()).isEqualTo(floorNumber);

            // 2.2 각 접두사별 검증
            for (PrefixInfo requestPrefix : requestFloor.prefixes()) {
                String prefix = requestPrefix.lockerPrefix();

                // 2.2.1 해당 접두사가 응답에 존재하는지 검증
                assertThat(findMatchingPrefixInfo(responseFloor.prefixes(), prefix)).isPresent();

                // 2.2.2 모든 사물함 번호 범위가 응답에 포함되어 있는지 검증
                for (LockerRange requestRange : requestPrefix.ranges()) {
                    int start = requestRange.lockerStartNumber();
                    int end = requestRange.lockerEndNumber();

                    assertThat(areAllNumbersInRange(responseFloor.prefixes(), prefix, start, end))
                            .isTrue();
                }
            }
        }
    }

    /** 특정 접두사와 일치하는 PrefixInfo를 찾습니다. null 접두사는 null과 일치시킵니다. */
    private Optional<PrefixInfo> findMatchingPrefixInfo(
            List<PrefixInfo> prefixes, String targetPrefix) {
        return prefixes.stream()
                .filter(p -> Objects.equals(targetPrefix, p.lockerPrefix()))
                .findFirst();
    }

    /** 지정된 접두사와 범위의 모든 번호가 응답에 포함되어 있는지 확인합니다. */
    private boolean areAllNumbersInRange(
            List<PrefixInfo> prefixes, String targetPrefix, int start, int end) {
        // 접두사가 일치하는 범위들 수집
        List<LockerRange> ranges =
                prefixes.stream()
                        .filter(p -> Objects.equals(p.lockerPrefix(), targetPrefix))
                        .flatMap(p -> p.ranges().stream())
                        .sorted(Comparator.comparing(LockerRange::lockerStartNumber))
                        .toList();

        // 현재까지 검증된 범위의 끝점
        int covered = start - 1;

        // 모든 범위를 순회하며 간격 없이 end까지 커버되는지 확인
        for (LockerRange range : ranges) {
            if (range.lockerStartNumber() > covered + 1) {
                return false; // 커버되지 않는 간격 발견
            }
            covered = Math.max(covered, range.lockerEndNumber());
            if (covered >= end) {
                return true; // 목표 범위를 모두 커버함
            }
        }

        return covered >= end; // 마지막 범위까지 확인 후 결과
    }

    // 다양한 층과 접두사, 범위를 가진 이벤트 생성 요청 준비
    private CreateEventRequest prepareComplexEventRequest(Long departmentId) {
        // 서로 다른 층, 접두사, 연속/불연속 범위를 포함한 복잡한 이벤트 요청 생성

        // 1층: 접두사 "A"와 "B", 각각 연속적인 범위
        List<LockerRange> rangesA =
                List.of(
                        new LockerRange(1, 5), // A-001 ~ A-005
                        new LockerRange(10, 15) // A-010 ~ A-015
                        );
        List<LockerRange> rangesB =
                List.of(
                        new LockerRange(1, 3) // B-001 ~ B-003
                        );
        List<PrefixInfo> prefixes1 =
                List.of(new PrefixInfo("A", rangesA), new PrefixInfo("B", rangesB));

        // 2층: 접두사 없음, 불연속적인 범위
        List<LockerRange> rangesNoPrefix =
                List.of(
                        new LockerRange(101, 103), // 101 ~ 103
                        new LockerRange(201, 205) // 201 ~ 205
                        );
        List<PrefixInfo> prefixes2 = List.of(new PrefixInfo(null, rangesNoPrefix));

        // 3층: 접두사 "X", 단일 범위
        List<LockerRange> rangesX =
                List.of(
                        new LockerRange(50, 55) // X-050 ~ X-055
                        );
        List<PrefixInfo> prefixes3 = List.of(new PrefixInfo("X", rangesX));

        List<FloorInfo> floors =
                List.of(
                        new FloorInfo(1, prefixes1),
                        new FloorInfo(2, prefixes2),
                        new FloorInfo(3, prefixes3));

        return createEventRequestBuilder()
                .withTitle("다양한 층과 접두사를 가진 이벤트")
                .withParticipationDepartmentIds(List.of(departmentId))
                .withFloors(floors)
                .build();
    }

    private static ValidatableResponse getEventDetail(UUID eventId, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(EVENT_URL + "/{event-id}", eventId)
                .then()
                .log()
                .ifError();
    }

    // 파라미터 제공 메서드
    private static Stream<Arguments> provideLockersPerFloor() {
        return Stream.of(
                Arguments.of(List.of(2, 2)), // 첫 번째 테스트 케이스: 층별 2개 사물함
                Arguments.of(List.of(3, 5)) // 두 번째 테스트 케이스: 1층 3개, 2층 5개
                );
    }

    // 이벤트 검색 메서드들
    public static ValidatableResponse getEventLockers(UUID eventId, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(EVENT_URL + "/{event-id}/lockers", eventId)
                .then()
                .log()
                .ifError();
    }

    public static ValidatableResponse getEvents(int page, int size, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("direction", DIRECTION)
                .queryParam("sort", SORT)
                .when()
                .get(EVENT_URL)
                .then()
                .log()
                .ifError();
    }

    public static ValidatableResponse getMyEventsWithPageable(
            String accessToken, EventPageable eventPageable) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .queryParam("page", eventPageable.getPage())
                .queryParam("size", eventPageable.getSize())
                .queryParam("direction", eventPageable.getDirection())
                .queryParam("sort", eventPageable.getSort())
                .when()
                .get(EVENT_URL + "/me")
                .then()
                .log()
                .ifError();
    }

    public static ValidatableResponse getMyEventDetail(UUID eventId, String accessToken) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .get(EVENT_URL + "/me/{event-id}", eventId)
                .then()
                .log()
                .ifError();
    }

    // 이벤트 생성 메서드들
    private void createEvent() {
        Department department = organizationTestUtil.createCouncilDepartment();
        createEventWithDepartment(department);
    }

    // 커스텀 이벤트 요청으로 이벤트 생성
    private void createCustomEvent(CreateEventRequest request) {
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .post(EVENT_URL)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    private void createEventWithDepartment(Department department) {
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

    // 유틸 메서드들
    private int calculateExpectedSize(int totalCount, int pageSize, int page) {
        int startIndex = page * pageSize;
        if (startIndex >= totalCount) {
            return 0; // 페이지가 범위를 벗어나면 빈 리스트
        }
        int remainingItems = totalCount - startIndex;
        return Math.min(remainingItems, pageSize);
    }

    private UUID getFirstEventId() {
        return getEvents(0, 10, accessToken)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(EventCustomPage.class)
                .content()
                .getFirst()
                .id();
    }

    // 이벤트 수정 메서드들
    private ValidatableResponse deleteEvent(UUID eventId) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .when()
                .delete(EVENT_URL + "/{event-id}", eventId)
                .then()
                .log()
                .ifError();
    }

    private ValidatableResponse publishEvent(UUID eventId, PublishEventRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .put(EVENT_URL + "/{event-id}/publish", eventId)
                .then()
                .log()
                .ifError();
    }

    private ValidatableResponse updateEvent(UUID eventId, UpdateEventRequest request) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, accessToken).build())
                .body(request)
                .when()
                .put(EVENT_URL + "/{event-id}", eventId)
                .then()
                .log()
                .ifError();
    }
}
