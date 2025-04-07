package com.jnulocker.organization.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.organization.exception.OrganizationNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetOrganizationsExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("단과대학 혹은 위원회가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 단과대학_혹은_위원회가_존재하지_않을_때 =
            OrganizationNotFoundException.EXCEPTION;
}
