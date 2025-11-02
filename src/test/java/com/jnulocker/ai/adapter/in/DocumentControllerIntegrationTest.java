package com.jnulocker.ai.adapter.in;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.jnulocker.ai.application.port.in.response.DocumentCustomPage;
import com.jnulocker.ai.application.port.in.response.DocumentDetailResponse;
import com.jnulocker.ai.application.port.in.response.UploadDocumentResponse;
import com.jnulocker.ai.domain.AiDocument;
import com.jnulocker.ai.exception.AiErrorCode;
import com.jnulocker.ai.utils.DocumentTestUtil;
import com.jnulocker.auth.utils.AuthTestUtil;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.config.RedisTest;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("문서 관리 컨트롤러 통합 테스트")
class DocumentControllerIntegrationTest extends RedisTest {

    private static final String DOCUMENT_URL = "/v1/ai/documents";
    private static final String ACCESS_TOKEN = "access_token";

    @LocalServerPort private int port;

    @Autowired private DocumentTestUtil documentTestUtil;

    @Autowired private MemberTestUtil memberTestUtil;

    @Autowired private AuthTestUtil authTestUtil;

    private static String accessToken;
    private static Member manager;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // MANAGER 사용자 생성 및 토큰 생성
        manager = memberTestUtil.createManager();
        accessToken = authTestUtil.generateAccessTokenWithMember(manager);
    }

    @AfterEach
    void tearDown() {
        // ElasticSearch의 VectorStore 데이터를 먼저 삭제
        documentTestUtil.deleteAllVectorStoreData();
        // 그 다음 DB의 문서 및 회원 데이터 삭제
        documentTestUtil.deleteAll();
        memberTestUtil.deleteAll();
    }

    // ===== 문서 업로드 테스트 =====

    @Test
    void MANAGER가_PDF_파일을_업로드할_수_있다() {
        // given
        MockMultipartFile pdfFile = documentTestUtil.createMockPdfFile("test.pdf");

        // when
        UploadDocumentResponse response =
                uploadDocument(pdfFile, null, accessToken)
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(UploadDocumentResponse.class);

        // then
        assertThat(response.documentId()).isNotNull();
        assertThat(response.fileName()).contains(".pdf");
        assertThat(response.chunkCount()).isGreaterThan(0);
        assertThat(response.uploadedAt()).isNotNull();
    }

    @Test
    void MANAGER가_TXT_파일을_업로드할_수_있다() {
        // given
        MockMultipartFile txtFile = documentTestUtil.createMockTxtFile("test.txt");

        // when
        UploadDocumentResponse response =
                uploadDocument(txtFile, null, accessToken)
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(UploadDocumentResponse.class);

        // then
        assertThat(response.documentId()).isNotNull();
        assertThat(response.fileName()).contains(".txt");
        assertThat(response.chunkCount()).isGreaterThan(0);
        assertThat(response.uploadedAt()).isNotNull();
    }

    @Test
    void 카테고리와_함께_문서를_업로드할_수_있다() {
        // given
        MockMultipartFile pdfFile = documentTestUtil.createMockPdfFile("manual.pdf");
        String category = "manual";

        // when
        UploadDocumentResponse response =
                uploadDocument(pdfFile, category, accessToken)
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(UploadDocumentResponse.class);

        // then
        assertThat(response.documentId()).isNotNull();
        assertThat(response.fileName()).contains(".pdf");
    }

    @Test
    void 지원하지_않는_파일_형식은_업로드할_수_없다() {
        // given
        MockMultipartFile unsupportedFile = documentTestUtil.createMockUnsupportedFile("image.jpg");

        // when
        ErrorResponse errorResponse =
                uploadDocument(unsupportedFile, null, accessToken)
                        .statusCode(AiErrorCode.UNSUPPORTED_FILE_TYPE.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AiErrorCode.UNSUPPORTED_FILE_TYPE.getMessage());
    }

    @Test
    void 빈_파일은_업로드할_수_없다() {
        // given
        MockMultipartFile emptyFile = documentTestUtil.createEmptyMockFile("empty.pdf");

        // when
        ErrorResponse errorResponse =
                uploadDocument(emptyFile, null, accessToken)
                        .statusCode(AiErrorCode.EMPTY_FILE.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(AiErrorCode.EMPTY_FILE.getMessage());
    }

    @Test
    void USER_권한으로는_문서를_업로드할_수_없다() {
        // given
        String userAccessToken = authTestUtil.generateAccessToken(Role.USER);
        MockMultipartFile pdfFile = documentTestUtil.createMockPdfFile("test.pdf");

        // when, then
        uploadDocument(pdfFile, null, userAccessToken).statusCode(HttpStatus.FORBIDDEN.value());
    }

    // ===== 문서 목록 조회 테스트 =====

    @ParameterizedTest(name = "[{index}] 조회[총: {0}, 크기: {1}, 페이지: {2}] -> 마지막: {3}")
    @CsvSource({
        "10, 5, 0, false", // 10개 생성, 5개씩, 0페이지(1~5), 마지막 아님
        "10, 5, 1, true", // 10개 생성, 5개씩, 1페이지(6~10), 마지막
        "8, 3, 0, false", // 8개 생성, 3개씩, 0페이지(1~3), 마지막 아님
        "8, 3, 2, true", // 8개 생성, 3개씩, 2페이지(7~8), 마지막
        "5, 10, 0, true" // 5개 생성, 10개씩, 0페이지(1~5), 마지막
    })
    void 문서_목록을_페이징하여_조회할_수_있다(int createCount, int pageSize, int page, boolean expectedLast) {
        // given
        documentTestUtil.createMultipleDocuments(manager, createCount, null);

        // when
        DocumentCustomPage documentPage =
                getDocuments(page, pageSize, null, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(DocumentCustomPage.class);

        // then
        int expectedSize = calculateExpectedSize(createCount, pageSize, page);
        assertThat(documentPage.content()).hasSize(expectedSize);
        assertThat(documentPage.totalElements()).isEqualTo(createCount);
        assertThat(documentPage.last()).isEqualTo(expectedLast);
    }

    @Test
    void 카테고리_필터로_문서를_조회할_수_있다() {
        // given
        documentTestUtil.createMultipleDocuments(manager, 5, "manual");
        documentTestUtil.createMultipleDocuments(manager, 3, "guide");

        // when
        DocumentCustomPage manualDocuments =
                getDocuments(0, 10, "manual", accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(DocumentCustomPage.class);

        // then
        assertThat(manualDocuments.content()).hasSize(5);
        assertThat(manualDocuments.totalElements()).isEqualTo(5);
        assertThat(manualDocuments.content()).allMatch(doc -> "manual".equals(doc.category()));
    }

    @Test
    void 빈_목록을_정상적으로_반환한다() {
        // given
        // 문서를 생성하지 않음

        // when
        DocumentCustomPage emptyPage =
                getDocuments(0, 10, null, accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(DocumentCustomPage.class);

        // then
        assertThat(emptyPage.content()).isEmpty();
        assertThat(emptyPage.totalElements()).isZero();
        assertThat(emptyPage.last()).isTrue();
    }

    // ===== 문서 상세 조회 테스트 =====

    @Test
    void 문서_상세_정보를_조회할_수_있다() {
        // given
        AiDocument document = documentTestUtil.createDocument(manager, "manual");

        // when
        DocumentDetailResponse response =
                getDocument(document.getId(), accessToken)
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(DocumentDetailResponse.class);

        // then
        assertThat(response.documentId()).isEqualTo(document.getId());
        assertThat(response.fileName()).isEqualTo(document.getFileName());
        assertThat(response.originalFileName()).isEqualTo(document.getOriginalFileName());
        assertThat(response.fileSize()).isEqualTo(document.getFileSize());
        assertThat(response.fileType()).isEqualTo(document.getFileType());
        assertThat(response.category()).isEqualTo(document.getCategory());
        assertThat(response.chunkCount()).isEqualTo(document.getChunkCount());
        assertThat(response.uploadedBy()).isEqualTo(manager.getId());
        assertThat(response.vectorStoreIds()).isNotNull();
    }

    @Test
    void 존재하지_않는_문서는_조회할_수_없다() {
        // given
        Long nonExistentDocumentId = 999999L;

        // when
        ErrorResponse errorResponse =
                getDocument(nonExistentDocumentId, accessToken)
                        .statusCode(AiErrorCode.DOCUMENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(AiErrorCode.DOCUMENT_NOT_FOUND.getMessage());
    }

    // ===== 문서 삭제 테스트 =====

    @Test
    void 본인이_업로드한_문서를_삭제할_수_있다() {
        // given
        AiDocument document = documentTestUtil.createDocument(manager, "manual");

        // when
        deleteDocument(document.getId(), accessToken).statusCode(HttpStatus.NO_CONTENT.value());

        // then
        // 삭제된 문서 조회 시 예외 발생
        ErrorResponse errorResponse =
                getDocument(document.getId(), accessToken)
                        .statusCode(AiErrorCode.DOCUMENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        assertThat(errorResponse.message()).isEqualTo(AiErrorCode.DOCUMENT_NOT_FOUND.getMessage());
    }

    @Test
    void 다른_사람이_업로드한_문서는_삭제할_수_없다() {
        // given
        Member uploader = memberTestUtil.createManager();
        Member otherManager = memberTestUtil.createManager();

        AiDocument document = documentTestUtil.createDocument(uploader, "manual");
        String otherManagerAccessToken = authTestUtil.generateAccessTokenWithMember(otherManager);

        // when
        ErrorResponse errorResponse =
                deleteDocument(document.getId(), otherManagerAccessToken)
                        .statusCode(
                                AiErrorCode.UNAUTHORIZED_DOCUMENT_ACCESS.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message())
                .isEqualTo(AiErrorCode.UNAUTHORIZED_DOCUMENT_ACCESS.getMessage());
    }

    @Test
    void 존재하지_않는_문서는_삭제할_수_없다() {
        // given
        Long nonExistentDocumentId = 999999L;

        // when
        ErrorResponse errorResponse =
                deleteDocument(nonExistentDocumentId, accessToken)
                        .statusCode(AiErrorCode.DOCUMENT_NOT_FOUND.getHttpStatus().value())
                        .extract()
                        .as(ErrorResponse.class);

        // then
        assertThat(errorResponse.message()).isEqualTo(AiErrorCode.DOCUMENT_NOT_FOUND.getMessage());
    }

    // ===== Helper Methods =====

    /** 문서 업로드 요청 */
    private ValidatableResponse uploadDocument(
            MockMultipartFile file, String category, String token) {
        try {
            var request =
                    given().contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .cookie(new Cookie.Builder(ACCESS_TOKEN, token).build())
                            .multiPart(
                                    "file",
                                    file.getOriginalFilename(),
                                    file.getBytes(),
                                    file.getContentType());

            if (category != null) {
                request.formParam("category", category);
            }

            return request.when().post(DOCUMENT_URL).then().log().ifError();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload document", e);
        }
    }

    /** 문서 목록 조회 요청 */
    private ValidatableResponse getDocuments(int page, int size, String category, String token) {
        var request =
                given().contentType(MediaType.APPLICATION_JSON_VALUE)
                        .cookie(new Cookie.Builder(ACCESS_TOKEN, token).build())
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("direction", "DESC")
                        .queryParam("sort", "createdAt");

        if (category != null) {
            request.queryParam("category", category);
        }

        return request.when().get(DOCUMENT_URL).then().log().ifError();
    }

    /** 문서 상세 조회 요청 */
    private ValidatableResponse getDocument(Long documentId, String token) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, token).build())
                .when()
                .get(DOCUMENT_URL + "/{document-id}", documentId)
                .then()
                .log()
                .ifError();
    }

    /** 문서 삭제 요청 */
    private ValidatableResponse deleteDocument(Long documentId, String token) {
        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(new Cookie.Builder(ACCESS_TOKEN, token).build())
                .when()
                .delete(DOCUMENT_URL + "/{document-id}", documentId)
                .then()
                .log()
                .ifError();
    }

    /** 예상 페이지 크기 계산 */
    private int calculateExpectedSize(int totalCount, int pageSize, int page) {
        int startIndex = page * pageSize;
        if (startIndex >= totalCount) {
            return 0; // 페이지가 범위를 벗어나면 빈 리스트
        }
        int remainingItems = totalCount - startIndex;
        return Math.min(remainingItems, pageSize);
    }
}
