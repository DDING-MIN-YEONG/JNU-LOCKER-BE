package com.jnulocker.announce.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateAnnounceExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("공지사항 주관 학과가 존재하지 않거나 공지사항 참여 학과가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 공지사항_주관_학과가_존재하지_않거나_공지사항_참여_학과가_존재하지_않을_때 =
            DepartmentNotFoundException.EXCEPTION;
}
