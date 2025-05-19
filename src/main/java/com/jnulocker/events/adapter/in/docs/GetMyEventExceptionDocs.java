package com.jnulocker.events.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.events.exception.EventNotPublishedException;
import com.jnulocker.events.exception.OnlyParticipationDepartmentCanSeeEventException;
import com.jnulocker.member.exception.MemberNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMyEventExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("회원 정보를 조회할 수 없을 때 발생하는 예외입니다")
    public static final BusinessException 회원_정보를_조회할_수_없을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("이벤트가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 이벤트가_존재하지_않을_때 = EventNotFoundException.EXCEPTION;

    @ExplainError("이벤트 참여 학과 소속이 아닐 때 발생하는 예외입니다")
    public static final BusinessException 이벤트_참여_학과_소속이_아닐_때 =
            OnlyParticipationDepartmentCanSeeEventException.EXCEPTION;

    @ExplainError("이벤트가 게시(publish)되지 않았을 때 발생하는 예외입니다")
    public static final BusinessException 이벤트가_게시되지_않았을_때 = EventNotPublishedException.EXCEPTION;
}
