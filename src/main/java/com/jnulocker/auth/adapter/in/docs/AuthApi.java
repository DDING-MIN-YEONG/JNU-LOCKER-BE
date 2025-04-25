package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @ApiExceptionExamples(LoginExcepitonDocs.class)
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response);

    @Operation(summary = "토큰 재발급", description = "refreshToken을 이용하여 토큰을 재발급합니다.")
    @ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response);
}
