package com.jnulocker.announce.adapter.in.docs;

import com.jnulocker.announce.exception.AnnounceParticipationNotFoundException;
import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.swagger.ExceptionDoc;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExceptionDoc;
import com.jnulocker.member.exception.MemberNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@ExceptionDoc
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetMyAnnounceExceptionDocs implements SwaggerExceptionDoc {
    @ExplainError("회원 정보를 조회할 수 없을 때 발생하는 예외입니다")
    public static final BusinessException 회원_정보를_조회할_수_없을_때 = MemberNotFoundException.EXCEPTION;

    @ExplainError("공지사항 참여 정보가 존재하지 않을 때 발생하는 예외입니다")
    public static final BusinessException 공지사항_참여_정보가_존재하지_않을_때 =
            AnnounceParticipationNotFoundException.EXCEPTION;
}
