package com.jnulocker.ai.adapter.in.docs;

import com.jnulocker.ai.exception.DocumentNotFoundException;
import com.jnulocker.ai.exception.UnauthorizedDocumentAccessException;
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
public class DeleteDocumentExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("회원 정보를 조회할 수 없을 때 발생하는 예외입니다")
    public static final BusinessException 회원_정보를_조회할_수_없을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("문서를 찾을 수 없을 때 발생하는 예외입니다")
    public static final BusinessException 문서를_찾을_수_없을_때 = DocumentNotFoundException.EXCEPTION;

    @ExplainError("본인이 업로드한 문서가 아닐 때 발생하는 예외입니다")
    public static final BusinessException 본인_문서가_아닐_때 =
            UnauthorizedDocumentAccessException.EXCEPTION;

    @ExplainError("Vector Store에서 문서를 삭제하는 중 오류가 발생했을 때 발생하는 예외입니다")
    public static final BusinessException VECTOR_STORE_삭제_실패 = VectorStoreException.DELETE_FAILED;
}
