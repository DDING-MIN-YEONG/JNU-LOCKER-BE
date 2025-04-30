package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.exception.SendEmailException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailSendExceptionDocs implements SwaggerExceptionDoc {
    @ExplainError("이메일 전송 중 오류가 발생했을 때 발생하는 예외입니다.")
    public static final BusinessException 이메일_전송_중_오류가_발생했을_때 = SendEmailException.EXCEPTION;
}
