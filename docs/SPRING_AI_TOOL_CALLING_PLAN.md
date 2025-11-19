# Spring AI Tool Calling 구현 계획서

> **최종 업데이트:** 2025-11-10
> **현재 진행 상태:** Phase 1 완료 ✅ → Phase 2 완료 ✅ → Phase 3 대기 중

### 1. Spring AI Tool Calling 개요

**Tool Calling이란?**
- AI 모델이 자연어 질의를 해석하여 적절한 함수/도구를 자동으로 호출하는 기능
- LLM이 단순 텍스트 응답을 넘어 실제 시스템 기능을 실행할 수 있도록 지원
- Spring AI에서는 `@Tool` 어노테이션, `FunctionToolCallback`, `ToolCallback` 인터페이스를 통해 구현

**핵심 메커니즘:**
1. 사용자가 자연어로 질의 (예: "다음 사물함 신청 이벤트 일정 알려줘")
2. LLM이 질의를 분석하여 필요한 Tool 식별
3. Tool이 실제 비즈니스 로직 실행 (EventQuery.getAllEvents() 호출)
4. 결과를 LLM이 자연어로 포맷팅하여 응답

### 2. 헥사고날 아키텍처와의 완벽한 시너지

**현재 JNU-LOCKER의 Port 기반 설계가 Tool Calling에 이상적인 이유:**

1. **Port Interface = Tool 후보**
   - 각 Port는 명확한 단일 책임을 가진 유스케이스
   - 입출력이 명확한 DTO로 정의됨
   - 비즈니스 로직이 Service Layer에 캡슐화되어 있어 Tool에서 안전하게 호출 가능

2. **의존성 역전의 장점**
   - Tool 클래스가 Port에만 의존하여 느슨한 결합 유지
   - 실제 구현체 변경에 영향받지 않음
   - 테스트 시 Mock Port를 사용하여 독립적 테스트 가능

3. **확장 용이성**
   - 새로운 Port 추가 시 해당 Tool만 추가하면 됨
   - 기존 코드 수정 없이 AI Agent 기능 확장 가능

### 3. 구현 가능한 AI Agent Tools (도메인별)

#### 3.1 Events Domain Tools

**Tool 1: EventSearchTool**
- **Port 사용:** `EventQuery.getAllEvents(Pageable)`
- **기능:** 사물함 신청 이벤트 목록 조회
- **Tool 정의:**
```java
@Tool(description = "사물함 신청 이벤트 목록을 검색합니다. 진행 중, 예정, 종료된 모든 이벤트를 조회할 수 있습니다.")
public String searchEvents(
    @ToolParam(description = "페이지 번호 (0부터 시작)", required = false) Integer page,
    @ToolParam(description = "페이지 크기", required = false) Integer size
)
```
- **사용 예시:** "다음 사물함 신청 언제야?", "진행 중인 이벤트 있어?"

**Tool 2: EventDetailTool**
- **Port 사용:** `EventQuery.getEvent(UUID)`
- **기능:** 특정 이벤트 상세 정보 조회
- **Tool 정의:**
```java
@Tool(description = "특정 사물함 신청 이벤트의 상세 정보를 조회합니다. 이벤트 제목, 시작/종료 시간, 참여 학과 등을 확인할 수 있습니다.")
public String getEventDetails(
    @ToolParam(description = "이벤트 ID (UUID 형식)", required = true) String eventId
)
```
- **사용 예시:** "이 이벤트 상세 정보 보여줘", "신청 기간이 언제까지야?"

**Tool 3: LockerAvailabilityTool**
- **Port 사용:** `EventQuery.getLockersByEventId(UUID)`
- **기능:** 이벤트별 사물함 가용성 조회
- **Tool 정의:**
```java
@Tool(description = "특정 이벤트의 사물함 가용 현황을 조회합니다. 층별로 사용 가능한 사물함과 이미 신청된 사물함을 확인할 수 있습니다.")
public String checkLockerAvailability(
    @ToolParam(description = "이벤트 ID", required = true) String eventId
)
```
- **사용 예시:** "몇 층에 빈 사물함 있어?", "3층 사물함 남은 거 있어?"

**Tool 4: MyEventsSearchTool**
- **Port 사용:** `EventQuery.getMyEvents(Pageable)`
- **기능:** 내가 참여 가능한 이벤트 조회
- **Tool 정의:**
```java
@Tool(description = "현재 사용자가 참여 가능한 사물함 신청 이벤트 목록을 조회합니다. 사용자의 소속 학과에 해당하는 이벤트만 표시됩니다.")
public String getMyEvents(
    @ToolParam(description = "페이지 번호", required = false) Integer page
)
```
- **사용 예시:** "내가 신청할 수 있는 이벤트 보여줘"

#### 3.2 Registration Domain Tools

**Tool 5: MyRegistrationTool**
- **Port 사용:** `RegistrationQuery.getMyRegistration(UUID)`
- **기능:** 내 사물함 신청 현황 조회
- **Tool 정의:**
```java
@Tool(description = "특정 이벤트에 대한 나의 사물함 신청 현황을 조회합니다. 신청한 사물함 번호, 위치, 신청 시간 등을 확인할 수 있습니다.")
public String checkMyRegistration(
    @ToolParam(description = "이벤트 ID", required = true) String eventId
)
```
- **사용 예시:** "내가 신청한 사물함 뭐였지?", "내 사물함 번호 알려줘"

**Tool 6: RegistrationListTool** (MANAGER 전용)
- **Port 사용:** `RegistrationQuery.getRegistrations(UUID, Pageable)`
- **기능:** 이벤트별 전체 신청 현황 조회
- **Tool 정의:**
```java
@Tool(description = "[관리자 전용] 특정 이벤트의 전체 사물함 신청 현황을 조회합니다. 신청자 정보, 신청 시간, 사물함 배정 상태 등을 확인할 수 있습니다.")
public String getRegistrationList(
    @ToolParam(description = "이벤트 ID", required = true) String eventId,
    @ToolParam(description = "페이지 번호", required = false) Integer page
)
```
- **사용 예시:** "이번 이벤트 신청자 몇 명이야?", "신청 현황 보여줘"

**Tool 7: RegisterForEventTool**
- **Port 사용:** `RegistrationCommand.registerForEvent(UUID, RegisterForEventRequest)`
- **기능:** 사물함 신청 실행
- **Tool 정의:**
```java
@Tool(description = "사물함 신청 이벤트에 참여하여 원하는 사물함을 신청합니다. 신청 가능 시간과 중복 신청 여부를 자동으로 검증합니다.")
public String registerForLocker(
    @ToolParam(description = "이벤트 ID", required = true) String eventId,
    @ToolParam(description = "사물함 ID", required = true) String lockerId
)
```
- **사용 예시:** "3층 301번 사물함 신청해줘", "이 사물함으로 신청할게"

**Tool 8: CancelRegistrationTool**
- **Port 사용:** `RegistrationCommand.cancelMyRegistration(UUID)`
- **기능:** 사물함 신청 취소
- **Tool 정의:**
```java
@Tool(description = "신청한 사물함을 취소합니다. 신청 취소 가능 기간 내에만 실행할 수 있습니다.")
public String cancelRegistration(
    @ToolParam(description = "이벤트 ID", required = true) String eventId
)
```
- **사용 예시:** "사물함 신청 취소할래", "신청 취소해줘"

#### 3.3 Announce Domain Tools

**Tool 9: AnnounceSearchTool**
- **Port 사용:** `AnnounceQuery.getAnnounces(Pageable)`
- **기능:** 공지사항 목록 조회
- **Tool 정의:**
```java
@Tool(description = "전체 공지사항 목록을 조회합니다. 최신 공지사항부터 시간 순으로 정렬되어 표시됩니다.")
public String searchAnnouncements(
    @ToolParam(description = "페이지 번호", required = false) Integer page
)
```
- **사용 예시:** "최근 공지사항 보여줘", "새로운 공지 있어?"

**Tool 10: AnnounceDetailTool**
- **Port 사용:** `AnnounceQuery.getAnnounce(Long)`
- **기능:** 공지사항 상세 조회
- **Tool 정의:**
```java
@Tool(description = "특정 공지사항의 상세 내용을 조회합니다. 제목, 내용, 작성자, 작성 시간 등을 확인할 수 있습니다.")
public String getAnnouncementDetail(
    @ToolParam(description = "공지사항 ID", required = true) Long announceId
)
```
- **사용 예시:** "이 공지 내용 읽어줘", "공지사항 자세히 보여줘"

#### 3.4 Organization Domain Tools

**Tool 11: OrganizationSearchTool**
- **Port 사용:** `OrganizationQuery.getOrganizations(OrganizationType)`
- **기능:** 조직(단과대학) 목록 조회
- **Tool 정의:**
```java
@Tool(description = "전남대학교의 단과대학 또는 기관 목록을 조회합니다. 단과대학, 대학원, 기타 기관을 구분하여 조회할 수 있습니다.")
public String searchOrganizations(
    @ToolParam(description = "조직 타입: COLLEGE(단과대학), GRADUATE_SCHOOL(대학원), OTHER(기타)", required = false) String organizationType
)
```
- **사용 예시:** "단과대 목록 보여줘", "어떤 단과대학이 있어?"

**Tool 12: DepartmentSearchTool**
- **Port 사용:** `DepartmentQuery.getDepartmentsByOrganizationId(Long)`
- **기능:** 조직별 학과/학부 목록 조회
- **Tool 정의:**
```java
@Tool(description = "특정 단과대학 또는 기관에 속한 학과/학부 목록을 조회합니다.")
public String searchDepartments(
    @ToolParam(description = "단과대학 ID", required = true) Long organizationId
)
```
- **사용 예시:** "공과대학에 어떤 학과들이 있어?", "자연대 학과 목록 알려줘"

#### 3.5 Member Domain Tools

**Tool 13: MyInfoTool**
- **Port 사용:** `MemberQuery.getMemberInfo()`
- **기능:** 내 정보 조회
- **Tool 정의:**
```java
@Tool(description = "현재 로그인한 사용자의 정보를 조회합니다. 이름, 이메일, 소속 학과, 학번 등을 확인할 수 있습니다.")
public String getMyInfo()
```
- **사용 예시:** "내 정보 알려줘", "내 학과가 뭐였지?"

### 4. 구현 아키텍처

#### 4.1 패키지 구조 (✅ Phase 1 완료)

**현재 구현된 구조:**
```
com.jnulocker.ai/
├── application/
│   └── tools/              # AI Tool 클래스들
│       ├── EventTools.java                 ✅ (searchEvents, getMyEvents)
│       ├── AnnounceTools.java              ✅ (searchAnnouncements)
│       ├── MemberTools.java                ✅ (getMyInfo)
│       ├── AiToolMethod.java               ✅ (커스텀 어노테이션)
│       └── AiToolErrorHandlingAspect.java  ✅ (AOP 통합 에러 핸들링)
├── exception/
│   └── AiToolExecutionException.java       ✅ (Tool 실행 예외)
└── (최상위 com.jnulocker.config)
    └── AiConfig.java       ✅ Tool 등록 + RAG 통합
```

**최종 구조 (Phase 2 완료):**
```
com.jnulocker.ai/
├── application/
│   └── tools/
│       ├── EventTools.java                 ✅ 완료 (Phase 1 + Phase 2 확장)
│       ├── AnnounceTools.java              ✅ 완료 (Phase 1 + Phase 2 확장)
│       ├── MemberTools.java                ✅ 완료 (Phase 1)
│       ├── RegistrationTools.java          ✅ 완료 (Phase 2 - 권한 검증 포함)
│       ├── OrganizationTools.java          ✅ 완료 (Phase 2)
│       ├── AiToolMethod.java               ✅ 완료 (Phase 1)
│       └── AiToolErrorHandlingAspect.java  ✅ 완료 (Phase 1)
├── exception/
│   └── AiToolExecutionException.java       ✅ 완료
└── (최상위 com.jnulocker.config)
    └── AiConfig.java       ✅ 완료 (Phase 1 + Phase 2 업데이트)
```

#### 4.2 Tool 구현 패턴 (✅ Phase 1 완료)

**실제 구현된 구조 (EventTools 예시):**
```java
@Component
@RequiredArgsConstructor
public class EventTools {

    private final EventQuery eventQuery;
    private final ObjectMapper objectMapper;

    @Tool(description = "사물함 신청 이벤트 목록을 검색합니다. 진행 중, 예정, 종료된 모든 이벤트를 조회할 수 있습니다.")
    @AiToolMethod  // AOP 기반 통합 로깅 및 예외 처리
    public String searchEvents(
        @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page,
        @ToolParam(description = "페이지 크기 (기본값 10)", required = false) Integer size
    ) throws JsonProcessingException {
        Pageable pageable = new EventPageable(page, size, null, null).toPageable();
        EventCustomPage result = eventQuery.getAllEvents(pageable);
        return objectMapper.writeValueAsString(result);
    }

    @Tool(description = "현재 로그인한 사용자가 참여(신청) 가능한 사물함 신청 이벤트 목록을 조회합니다.")
    @AiToolMethod
    public String getMyEvents(
        @ToolParam(description = "페이지 번호 (0부터 시작, 기본값 0)", required = false) Integer page
    ) throws JsonProcessingException {
        Pageable pageable = new EventPageable(page, null, null, null).toPageable();
        MyEventCustomPage result = eventQuery.getMyEvents(pageable);
        return objectMapper.writeValueAsString(result);
    }
}
```

**핵심 설계 원칙:**
1. **Port 의존:** Service가 아닌 Port Interface에 의존 ✅
2. **DTO 직렬화:** 결과를 JSON 문자열로 반환 (LLM이 파싱하여 자연어로 변환) ✅
3. **예외 처리:** `@AiToolMethod` AOP로 통합 처리 (횡단 관심사 분리) ✅
4. **선택적 파라미터:** required=false로 기본값 제공 ✅
5. **로깅:** AOP Aspect에서 자동 로깅 (모니터링 및 디버깅 용이) ✅
6. **도메인 Pageable 활용:** EventPageable, AnnouncePageable 등 도메인별 Pageable 구현체 사용 ✅

**AOP 방식의 장점:**
- Tool 메서드는 비즈니스 로직만 집중 (단일 책임 원칙)
- 중복 코드 제거 (DRY 원칙)
- 일관된 로깅 및 에러 메시지 포맷
- 새로운 Tool 추가 시 `@AiToolMethod`만 붙이면 자동으로 로깅 및 예외 처리 적용

#### 4.3 ChatClient 통합 (✅ Phase 1 완료)

**실제 구현된 AiConfig:**
```java
@Configuration
public class AiConfig {

    @Value("classpath:/prompts/system-template.st")
    private Resource systemTemplate;

    @Bean
    public ChatClient chatClient(
        ChatClient.Builder builder,
        VectorStore vectorStore,
        EventTools eventTools,
        AnnounceTools announceTools,
        MemberTools memberTools
    ) {
        return builder
            .defaultSystem(systemTemplate)
            .defaultAdvisors(
                QuestionAnswerAdvisor.builder(vectorStore)
                    .searchRequest(
                        SearchRequest.builder()
                            .topK(5)  // 상위 5개 유사 문서 검색
                            .similarityThreshold(0.5)
                            .build()
                    )
                    .build()
            )
            .defaultTools(eventTools, announceTools, memberTools)  // Phase 1 Tools 등록 ✅
            .build();
    }
}
```

**실제 구현된 AiChatCommandService:**
```java
@Service
@RequiredArgsConstructor
public class AiChatCommandService implements AiChatCommand {

    private final ChatClient client;

    @Override
    public AiChatResponse chat(AiChatRequest request) {
        // ChatClient가 자동으로 RAG + Tool Calling 처리
        String message = client.prompt()
            .user(request.message())
            .call()
            .content();

        return AiChatResponse.of(message);
    }
}
```

**주요 개선 사항:**
1. **RAG 통합:** QuestionAnswerAdvisor로 VectorStore 검색을 자동화 ✅
2. **System Template 분리:** `system-template.st` 파일로 프롬프트 관리 ✅
3. **단순화된 Service:** RAG 컨텍스트 관리를 Advisor에 위임하여 코드 간소화 ✅
4. **명시적 Tool 등록:** Phase별로 추가 가능한 구조 ✅

#### 4.4 권한 제어

**ToolContext를 활용한 권한 검증:**
```java
@Component
@RequiredArgsConstructor
public class RegistrationListTool {

    private final RegistrationQuery registrationQuery;
    private final MemberQuery memberQuery;

    @Tool(description = "[관리자 전용] 이벤트별 전체 신청 현황 조회")
    public String getRegistrationList(
        @ToolParam(description = "이벤트 ID", required = true) String eventId,
        ToolContext context
    ) {
        // ToolContext에서 사용자 정보 추출
        Long userId = (Long) context.getContext().get("userId");
        Member member = memberQuery.findByIdOrThrow(userId);

        // 권한 검증
        if (member.getRole() != Role.MANAGER) {
            return "이 기능은 관리자만 사용할 수 있습니다.";
        }

        // 실제 로직 실행
        // ...
    }
}
```

#### 4.5 AOP 기반 횡단 관심사 처리 (✅ Phase 1 완료)

**@AiToolMethod 커스텀 어노테이션:**
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AiToolMethod {}
```

**AiToolErrorHandlingAspect 구현:**
```java
@Aspect
@Component
@Slf4j
public class AiToolErrorHandlingAspect {

    @Around("@annotation(aiToolMethod)")
    public Object handleToolErrors(ProceedingJoinPoint joinPoint, AiToolMethod aiToolMethod) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        try {
            // 1. Tool 호출 전 로깅
            log.info("[AI Tool] {}.{}({})", className, methodName, formatParameters(paramNames, args));

            // 2. 실제 Tool 실행
            return joinPoint.proceed();

        } catch (JsonProcessingException e) {
            // 3-1. JSON 직렬화 오류
            log.error("[AI Tool] JSON 직렬화 실패 - {}.{}", className, methodName, e);
            return formatErrorResponse(methodName, "조회");

        } catch (Exception e) {
            // 3-2. 일반 비즈니스 로직 오류
            log.error("[AI Tool] 실행 실패 - {}.{}", className, methodName, e);
            return formatErrorResponse(methodName, "처리");

        } catch (Throwable t) {
            // 3-3. 예상치 못한 시스템 오류
            log.error("[AI Tool] 예상치 못한 오류 - {}.{}", className, methodName, t);
            throw AiToolExecutionException.EXCEPTION;
        }
    }

    private String formatParameters(String[] names, Object[] values) {
        if (names.length == 0) return "";

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            params.put(names[i], values[i]);
        }

        return params.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
    }

    private String formatErrorResponse(String methodName, String action) {
        return String.format("%s %s 중 오류가 발생했습니다", methodName, action);
    }
}
```

**AOP 방식의 핵심 이점:**

1. **횡단 관심사 분리 (Cross-Cutting Concerns)**
   - 로깅, 예외 처리를 Tool 메서드에서 완전히 분리
   - 비즈니스 로직만 Tool 메서드에 집중 (단일 책임 원칙)

2. **중복 코드 제거 (DRY 원칙)**
   - 모든 Tool에 공통으로 적용되는 로직을 한 곳에서 관리
   - 새로운 Tool 추가 시 `@AiToolMethod`만 붙이면 자동 적용

3. **일관된 로깅 및 에러 처리**
   - 통일된 로그 포맷: `[AI Tool] ClassName.methodName(params)`
   - 3단계 예외 처리 전략으로 안정성 확보

4. **유지보수성 향상**
   - 로깅 포맷 변경 시 Aspect만 수정하면 모든 Tool에 자동 반영
   - 예외 처리 전략 변경 시에도 한 곳만 수정

**3단계 예외 처리 전략:**
- **Level 1**: `JsonProcessingException` → 사용자 친화적 메시지 반환
- **Level 2**: `Exception` → 일반 에러 메시지 반환
- **Level 3**: `Throwable` → `AiToolExecutionException` 던져서 글로벌 핸들러로 전파

### 5. 기대 효과

#### 5.1 사용자 경험 향상
- **자연어 인터페이스:** 복잡한 UI 탐색 없이 대화로 모든 기능 사용
- **멀티스텝 작업 자동화:** "빈 사물함 찾아서 신청해줘" → 검색 + 신청 자동 실행
- **24/7 지원:** AI가 언제든 즉각 응답

#### 5.2 개발 생산성 향상
- **기존 코드 재사용:** Port Interface를 그대로 활용하여 개발 시간 단축
- **확장 용이성:** 새로운 Port 추가 시 Tool만 추가하면 AI 기능 자동 확장
- **테스트 용이성:** Port Mock을 통한 독립적 Tool 테스트

#### 5.3 시스템 안정성
- **검증된 비즈니스 로직:** 이미 운영 중인 Service Layer 사용
- **트랜잭션 안전성:** Port를 통한 호출로 기존 트랜잭션 경계 유지
- **권한 제어:** ToolContext를 통한 세밀한 접근 제어

### 6. 구현 단계

**✅ Phase 1: 기본 조회 Tools (완료)**
- ✅ EventTools.searchEvents() - 전체 이벤트 목록 조회
- ✅ EventTools.getMyEvents() - 내가 참여 가능한 이벤트 조회
- ✅ AnnounceTools.searchAnnouncements() - 공지사항 목록 조회
- ✅ MemberTools.getMyInfo() - 내 정보 조회
- ✅ ChatClient 통합 (RAG QuestionAnswerAdvisor + Tool Calling)
- ✅ 로깅 시스템 추가 (모든 Tool 호출 추적)

**✅ Phase 2: 상세 조회 Tools (완료)**
- ✅ EventTools에 추가:
  - getEventDetails(String eventId) - 이벤트 상세 정보
  - checkLockerAvailability(String eventId) - 사물함 가용성
- ✅ RegistrationTools 생성:
  - checkMyRegistration(String eventId) - 내 신청 현황
  - getRegistrationList(String eventId, Integer page) - 전체 신청 현황 (MANAGER 전용)
    - MemberQuery를 통한 권한 검증 구현 (MANAGER/ADMIN만 접근 가능)
    - 권한 미달 시 사용자 친화적 에러 메시지 반환
- ✅ OrganizationTools 생성:
  - searchOrganizations(String type) - 조직 목록 (COUNCIL/COMMITTEE 타입 지원)
    - Enum 타입 String 변환 처리
    - null/빈 문자열 시 전체 조직 조회
  - searchDepartments(Long orgId) - 학과 목록
- ✅ AnnounceTools에 추가:
  - getAnnouncementDetail(Long announceId) - 공지사항 상세
- ✅ AiConfig에 5개 Tools 통합 등록 (eventTools, announceTools, memberTools, registrationTools, organizationTools)

**Phase 3: Command Tools (예정)**
- RegisterForEventTool
- CancelRegistrationTool
- 권한 제어 로직 구현 (ToolContext 활용)
- End-to-End 테스트

**Phase 4: 고도화 (예정)**
- Tool 실행 메트릭 수집 및 모니터링
- 에러 핸들링 개선 (재시도 로직, Circuit Breaker)
- 성능 최적화 (캐싱, 불필요한 데이터 로딩 방지)
- 사용자 피드백 기반 Tool 개선

### 7. 기술적 고려사항

**7.1 응답 포맷**
- JSON 직렬화 vs 자연어 포맷팅
- LLM이 파싱하기 쉬운 구조화된 응답 설계

**7.2 성능**
- Tool 호출 시 불필요한 데이터 로딩 방지
- Pageable 기본값을 작게 설정 (10개)
- 캐싱 전략 고려

**7.3 보안**
- 민감 정보 마스킹 (개인정보 보호)
- SQL Injection 방지 (QueryDSL 사용으로 이미 안전)
- Rate Limiting (OpenAI API 비용 제어)

**7.4 모니터링**
- Tool 호출 빈도 추적
- 실패율 모니터링
- LLM이 적절한 Tool을 선택하는지 검증

### 8. Phase 1 완료 후기 및 개선 사항

#### 8.1 성공적인 구현 포인트

1. **헥사고날 아키텍처의 완벽한 시너지**
   - Port Interface를 그대로 활용하여 개발 시간 대폭 단축
   - EventQuery, AnnounceQuery, MemberQuery를 Tool에서 직접 주입받아 사용
   - 비즈니스 로직 재사용으로 안정성 확보

2. **도메인별 Pageable 구현체 활용**
   - `EventPageable`, `AnnouncePageable` 등 기존 도메인 객체 재사용
   - 일관된 페이징 처리 로직

3. **RAG 통합 자동화**
   - `QuestionAnswerAdvisor`로 VectorStore 검색을 자동화
   - Service Layer가 단순해지고 유지보수성 향상

4. **로깅을 통한 모니터링**
   - 모든 Tool 호출 시 파라미터를 로깅
   - 디버깅 및 사용 패턴 분석에 유용

5. **AOP 기반 횡단 관심사 분리**
   - `@AiToolMethod` 커스텀 어노테이션으로 통합 관리
   - `AiToolErrorHandlingAspect`로 로깅 및 예외 처리 일관성 확보
   - Tool 메서드는 비즈니스 로직만 집중 (Clean Code 원칙 준수)
   - **문서 작성 시점에는 계획에 없었으나, 구현 과정에서 아키텍처 개선됨**
   - 향후 Tool 추가 시 `@AiToolMethod`만 붙이면 자동으로 로깅 및 예외 처리 적용

#### 8.2 Phase 2 준비 사항

**추가할 Tools 우선순위:**
1. **EventTools 확장** (가장 중요)
   - `getEventDetails()` - 사용자가 가장 많이 물어볼 것으로 예상
   - `checkLockerAvailability()` - "빈 사물함 있어?" 질문 대응

2. **RegistrationTools 생성**
   - `checkMyRegistration()` - "내가 신청한 사물함 뭐였지?" 질문 대응
   - Phase 3에서 Command Tool (신청/취소) 추가 시 기반 작업

3. **AnnounceTools 확장**
   - `getAnnouncementDetail()` - 공지사항 상세 조회

#### 8.3 기술적 개선 아이디어

1. **응답 포맷 최적화**
   - 현재: 전체 DTO를 JSON으로 직렬화
   - 개선: LLM이 이해하기 쉬운 핵심 정보만 추출하여 반환
   - 예시: "진행 중인 이벤트 3건: [이벤트1], [이벤트2], [이벤트3]"

2. **Tool 호출 실패 시 재시도 로직**
   - 일시적 오류(타임아웃, DB 연결 끊김 등)에 대한 재시도 메커니즘
   - Spring Retry 또는 Resilience4j 활용 고려

3. **Tool 성능 모니터링**
   - 각 Tool 실행 시간 측정
   - 느린 Tool 식별 및 최적화

### 9. 참고 자료

- Spring AI 공식 문서: Function Calling
- Spring AI Examples: Tool Callback 패턴
- OpenAI Function Calling Best Practices
- Hexagonal Architecture with AI Integration Patterns

### 10. Phase 2 완료 후기 및 개선 사항

#### 10.1 성공적인 구현 포인트

1. **권한 검증 로직 통합**
   - `RegistrationTools.getRegistrationList()`에 권한 검증 구현
   - MemberQuery를 통한 현재 사용자 정보 조회
   - MANAGER/ADMIN 권한이 아닐 경우 친화적 에러 메시지 반환
   - Tool 내부에서 권한 검증을 직접 처리 (ToolContext 없이 간단하게 구현)

2. **확장 가능한 설계 검증**
   - Phase 1의 설계가 Phase 2 구현 시 변경 없이 확장됨
   - @AiToolMethod AOP 패턴이 모든 새로운 Tool에 자동 적용
   - 7개의 새로운 Tool 메서드가 일관된 패턴으로 추가됨

3. **도메인 Port 재사용**
   - EventQuery, RegistrationQuery, OrganizationQuery, DepartmentQuery, AnnounceQuery 재사용
   - 비즈니스 로직 중복 없이 AI Tool로 노출
   - 헥사고날 아키텍처의 장점이 명확히 드러남

4. **OrganizationType 처리**
   - Enum 타입을 String으로 받아 변환하는 패턴 구현
   - null/빈 문자열 처리로 선택적 파라미터 구현
   - AI가 자연어로 받은 조직 타입을 정확히 매핑

5. **페이지네이션 통합**
   - RegistrationPageable을 활용한 일관된 페이징 처리
   - 기본값 설정으로 사용자 편의성 향상

#### 10.2 Phase 3 준비 사항

**추가할 Command Tools (쓰기 작업):**
1. **RegisterForEventTool** (최우선)
   - Port: `RegistrationCommand.registerForEvent()`
   - 사물함 신청 기능
   - 트랜잭션 안전성 검증 필요

2. **CancelRegistrationTool**
   - Port: `RegistrationCommand.cancelMyRegistration()`
   - 사물함 신청 취소 기능
   - 취소 가능 기간 검증

3. **권한 제어 강화**
   - 쓰기 작업에 대한 권한 검증 강화
   - MANAGER 전용 Command Tools (추후 검토)

#### 10.3 Phase 2에서 발견된 개선 포인트

1. **권한 검증 패턴 통일 필요**
   - 현재: getRegistrationList()에만 권한 검증 구현
   - Phase 3에서 권한 검증 AOP 또는 공통 유틸 고려

2. **에러 메시지 표준화**
   - 권한 검증 실패 시 메시지 포맷 통일
   - 사용자 친화적 메시지 vs JSON 구조화된 에러

3. **통합 테스트 필요성**
   - Phase 2 Tools에 대한 통합 테스트 작성
   - AI가 올바른 Tool을 선택하는지 검증
   - Tool 간 연계 시나리오 테스트

---

**문서 변경 이력:**
- 2025-11-06: Phase 1 완료, 실제 구현 내용으로 문서 업데이트
- 2025-11-10: Phase 1 재검증 및 AOP 기반 횡단 관심사 처리 구현 반영
  - Section 4.1: 패키지 구조에 AOP 관련 클래스 추가
  - Section 4.2: Tool 구현 패턴을 AOP 방식으로 업데이트 (try-catch 제거)
  - Section 4.5: AOP 기반 횡단 관심사 처리 섹션 신규 추가
  - Section 8.1: AOP 구현 성공 포인트 추가
- 2025-11-10: Phase 2 완료 (같은 날 Phase 1 검증 후 Phase 2 구현)
  - 최상단 진행 상태: Phase 2 완료 표시
  - Section 4.1: 패키지 구조 Phase 2 완료 상태로 업데이트
  - Section 6: Phase 2 구현 단계 완료 표시 및 세부 내용 반영
  - Section 10: Phase 2 완료 후기 및 개선 사항 신규 추가 (권한 검증 패턴, OrganizationType 처리 등)
