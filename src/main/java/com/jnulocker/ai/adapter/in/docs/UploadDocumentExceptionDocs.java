package com.jnulocker.ai.adapter.in.docs;

import com.jnulocker.ai.exception.DocumentFileException;
import com.jnulocker.ai.exception.DocumentParseFailedException;
import com.jnulocker.ai.exception.EmptyFileException;
import com.jnulocker.ai.exception.VectorStoreException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.member.exception.MemberNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UploadDocumentExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("회원 정보를 조회할 수 없을 때 발생하는 예외입니다")
    public static final BusinessException 회원_정보를_조회할_수_없을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("파일이 비어있을 때 발생하는 예외입니다")
    public static final BusinessException 파일이_비어있을_때 = EmptyFileException.EXCEPTION;

    @ExplainError("파일 크기가 최대 허용 크기(10MB)를 초과했을 때 발생하는 예외입니다")
    public static final BusinessException 파일_크기_초과 = DocumentFileException.FILE_SIZE_EXCEEDED;

    @ExplainError("지원하지 않는 파일 형식일 때 발생하는 예외입니다 (PDF, TXT만 지원)")
    public static final BusinessException 지원하지_않는_파일_형식 =
            DocumentFileException.UNSUPPORTED_FILE_TYPE;

    @ExplainError("문서 파싱에 실패했을 때 발생하는 예외입니다")
    public static final BusinessException 문서_파싱_실패 = DocumentParseFailedException.EXCEPTION;

    @ExplainError("Vector Store에 문서를 저장하는 중 오류가 발생했을 때 발생하는 예외입니다")
    public static final BusinessException VECTOR_STORE_저장_실패 = VectorStoreException.SAVE_FAILED;
}
