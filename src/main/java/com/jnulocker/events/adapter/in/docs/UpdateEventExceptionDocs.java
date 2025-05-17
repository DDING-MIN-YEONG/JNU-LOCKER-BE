package com.jnulocker.events.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.events.exception.OnlyManagerCanUpdateEventException;
import com.jnulocker.events.exception.OnlyReadyEventCanBeUpdatedException;
import com.jnulocker.member.exception.MemberNotFoundException;
import com.jnulocker.organization.exception.DepartmentNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateEventExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("회원 정보를 조회할 수 없을 때 발생하는 예외입니다")
    public static final BusinessException 회원_정보를_조회할_수_없을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("이벤트가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 이벤트가_존재하지_않을_때 = EventNotFoundException.EXCEPTION;

    @ExplainError("이벤트 주최 학과가 아닐 때 발생하는 예외입니다")
    public static final BusinessException 이벤트_주최_학과가_아닐_때 =
            OnlyManagerCanUpdateEventException.EXCEPTION;

    @ExplainError("이벤트가 OPEN, CLOSE 상태에서 수정 요청이 들어올 때 발생하는 예외입니다")
    public static final BusinessException 이벤트가_OPEN_CLOSE_상태에서_수정_요청이_들어올_때 =
            OnlyReadyEventCanBeUpdatedException.EXCEPTION;

    @ExplainError("이벤트 주관 학과가 존재하지 않거나 이벤트 참여 학과가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 이벤트_주관_학과가_존재하지_않거나_이벤트_참여_학과가_존재하지_않을_때 =
            DepartmentNotFoundException.EXCEPTION;
}
