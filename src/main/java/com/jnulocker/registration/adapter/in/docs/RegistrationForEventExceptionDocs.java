package com.jnulocker.registration.adapter.in.docs;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.events.exception.EventNotFoundException;
import com.jnulocker.events.exception.EventNotOpenException;
import com.jnulocker.events.exception.InvalidLockerForEventException;
import com.jnulocker.events.exception.LockerNotFoundException;
import com.jnulocker.events.exception.LockerUnavailableException;
import com.jnulocker.member.exception.MemberNotFoundException;
import com.jnulocker.registration.exception.RegistrationAlreadyExistsException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationForEventExceptionDocs implements SwaggerExceptionDoc {

    @ExplainError("회원이 존재하지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 회원이_존재하지_않을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("이벤트가 존재하지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 이벤트가_존재하지_않을_때 = EventNotFoundException.EXCEPTION;

    @ExplainError("사물함이 존재하지 않을 때 발생하는 예외입니다.")
    public static final BusinessException 사물함이_존재하지_않을_때 = LockerNotFoundException.EXCEPTION;

    @ExplainError("이벤트에 해당하지 않는 사물함을 신청할 때 발생하는 예외입니다.")
    public static final BusinessException 이벤트에_해당하지_않는_사물함을_신청할_때 =
            InvalidLockerForEventException.EXCEPTION;

    @ExplainError("이미 해당 이벤트에서 사물함을 신청했을 때 발생하는 예외입니다.")
    public static final BusinessException 이미_해당_이벤트에서_사물함을_신청했을_때 =
            RegistrationAlreadyExistsException.EXCEPTION;

    @ExplainError(
            "이벤트가 열려있지 않을 때 발생하는 예외입니다. eventStatus가 OPEN이 아니거나, USER가 publish == false인 이벤트에서 신청을 요청하는 경우 발생합니다.")
    public static final BusinessException 이벤트가_열려있지_않을_때 = EventNotOpenException.EXCEPTION;

    @ExplainError("사물함이 사용중(available == false)일 때 발생하는 예외입니다.")
    public static final BusinessException 사물함이_사용중일_때 = LockerUnavailableException.EXCEPTION;
}
