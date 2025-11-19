package com.jnulocker.ai.exception;

import com.jnulocker.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AiErrorCode implements ErrorCode {
    DOCUMENT_NOT_FOUND("AI001", HttpStatus.NOT_FOUND, "문서를 찾을 수 없습니다."),
    UNSUPPORTED_FILE_TYPE(
            "AI002", HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다. PDF 또는 TXT 파일만 업로드 가능합니다."),
    FILE_SIZE_EXCEEDED("AI003", HttpStatus.BAD_REQUEST, "파일 크기가 너무 큽니다. 최대 10MB까지 업로드 가능합니다."),
    UNAUTHORIZED_DOCUMENT_ACCESS("AI004", HttpStatus.FORBIDDEN, "본인이 업로드한 문서만 접근할 수 있습니다."),
    VECTOR_STORE_SAVE_FAILED(
            "AI005", HttpStatus.INTERNAL_SERVER_ERROR, "Vector Store에 문서를 저장하는 중 오류가 발생했습니다."),
    VECTOR_STORE_DELETE_FAILED(
            "AI006", HttpStatus.INTERNAL_SERVER_ERROR, "Vector Store에서 문서를 삭제하는 중 오류가 발생했습니다."),
    EMPTY_FILE("AI007", HttpStatus.BAD_REQUEST, "파일이 비어있습니다."),
    DOCUMENT_PARSE_FAILED("AI008", HttpStatus.INTERNAL_SERVER_ERROR, "문서 파싱에 실패했습니다."),
    AI_TOOL_EXECUTION_ERROR(
            "AI009", HttpStatus.INTERNAL_SERVER_ERROR, "AI 도구 실행 중 예상치 못한 오류가 발생했습니다."),
    EMBEDDING_GENERATION_FAILED("AI010", HttpStatus.INTERNAL_SERVER_ERROR, "임베딩 생성에 실패했습니다."),
    EMBEDDING_API_CLIENT_ERROR("AI011", HttpStatus.BAD_REQUEST, "임베딩 API 호출 중 클라이언트 오류가 발생했습니다."),
    EMBEDDING_API_SERVER_ERROR("AI012", HttpStatus.INTERNAL_SERVER_ERROR, "임베딩 API 서버 오류가 발생했습니다."),
    EMPTY_EMBEDDING_RESPONSE("AI013", HttpStatus.INTERNAL_SERVER_ERROR, "임베딩 응답이 비어있습니다."),
    INVALID_EMBEDDING_INPUT("AI014", HttpStatus.BAD_REQUEST, "임베딩할 텍스트가 비어있습니다."),
    INVALID_UUID_FORMAT("AI015", HttpStatus.BAD_REQUEST, "올바른 UUID 형식이 아닙니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
