package com.jnulocker.events.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.events.exception.OnlyDepartmentManagerCanSeeEventException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetEventExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("이벤트가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 이벤트가_존재하지_않을_때 = EventNotFoundException.EXCEPTION;

    @ExplainError("이벤트 주최 학과의 구성원이 아닐 때 발생하는 예외입니다")
    public static final BusinessException 이벤트_주최_학과의_구성원이_아닐_때 =
            OnlyDepartmentManagerCanSeeEventException.EXCEPTION;
}
