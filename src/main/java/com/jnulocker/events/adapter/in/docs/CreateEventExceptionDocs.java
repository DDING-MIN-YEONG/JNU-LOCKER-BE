package com.jnulocker.events.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateEventExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("이벤트 주관 학과가 존재하지 않거나 이벤트 참여 학과가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 이벤트_주관_학과가_존재하지_않거나_이벤트_참여_학과가_존재하지_않을_때 =
            DepartmentNotFoundException.EXCEPTION;
}
