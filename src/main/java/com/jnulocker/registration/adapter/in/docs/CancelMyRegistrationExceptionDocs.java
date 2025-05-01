package com.jnulocker.registration.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.registration.exception.RegistrationNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CancelMyRegistrationExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("이벤트가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 이벤트가_존재하지_않을_때 = EventNotFoundException.EXCEPTION;

    @ExplainError("사물함 신청 내역이 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 신청_내역이_존재하지_않을_때 =
            RegistrationNotFoundException.EXCEPTION;
}
