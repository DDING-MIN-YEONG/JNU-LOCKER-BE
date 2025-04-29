package com.jnulocker.registration.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.registration.exception.RegistrationNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMyRegistrationExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("신청 정보가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 신청_정보가_존재하지_않을_때 =
            RegistrationNotFoundException.EXCEPTION;
}
