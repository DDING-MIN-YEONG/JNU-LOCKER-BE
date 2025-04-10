package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증", description = "인증 관련 API")
public interface AuthApi {

    @ApiExceptionExamples(SignupExceptionDocs.class)
    @Operation(summary = "USER 회원가입", description = "사물함을 신청하는 사용자인 USER에 대한 회원가입입니다.")
    @ApiResponse(responseCode = "201", description = "USER 회원가입 성공")
    ResponseEntity<Void> signupUser(@Valid @RequestBody UserSignupRequest request);

    @ApiExceptionExamples(SignupExceptionDocs.class)
    @Operation(summary = "MANAGER 회원가입", description = "사물함 신청 이벤트 관리자인 MANAGER에 대한 회원가입입니다.")
    @ApiResponse(responseCode = "201", description = "MANAGER 회원가입 성공")
    ResponseEntity<Void> signupManager(@Valid @RequestBody ManagerSignupRequest request);
}
