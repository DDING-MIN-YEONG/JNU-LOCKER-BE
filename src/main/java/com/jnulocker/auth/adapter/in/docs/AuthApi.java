package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.adapter.in.request.UserSignupReqDto;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증인가", description = "인증인가 관련 API")
public interface AuthApi {

    @ApiExceptionExamples(SignupExceptionDocs.class)
    @Operation(summary = "USER 회원가입", description = "사물함을 신청하는 사용자인 USER에 대한 회원가입입니다.")
    ResponseEntity<Void> signupUser(@Valid @RequestBody UserSignupReqDto userSignupReqDto);

}
