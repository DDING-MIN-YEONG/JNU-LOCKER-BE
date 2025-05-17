package com.jnulocker.auth.adapter.in.docs;

import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerApproveRequest;
import com.jnulocker.auth.application.port.in.request.ManagerRejectRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.SendEmailRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;
import com.jnulocker.auth.application.port.in.response.PendingManagerCustomPage;
import com.jnulocker.auth.application.port.in.response.PendingManagerPageable;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
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

    @ApiExceptionExamples(GetPendingManagerExceptionDocs.class)
    @Operation(summary = "가입 승인 대기중인 MANAGER 목록 조회", description = "가입 승인 대기중인 MANAGER 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "가입 승인 대기중인 MANAGER 목록 조회 성공",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PendingManagerCustomPage.class)))
    ResponseEntity<PendingManagerCustomPage> getPendingManagers(
            @Valid @ParameterObject PendingManagerPageable pendingManagerPageable);

    @ApiExceptionExamples(ApproveManagerExceptionDocs.class)
    @Operation(summary = "MANAGER 가입 승인", description = "MANAGER 가입을 승인합니다.")
    @ApiResponse(responseCode = "204", description = "MANAGER 가입 승인 성공")
    ResponseEntity<Void> approveManager(@Valid @RequestBody ManagerApproveRequest request);

    @ApiExceptionExamples(RejectManagerExceptionDocs.class)
    @Operation(summary = "MANAGER 가입 거절", description = "MANAGER 가입을 거절합니다.")
    @ApiResponse(responseCode = "204", description = "MANAGER 가입 거절 성공")
    ResponseEntity<Void> rejectManager(@Valid @RequestBody ManagerRejectRequest request);

    @ApiExceptionExamples(LoginExcepitonDocs.class)
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response);

    @ApiExceptionExamples(ReissueExceptionDocs.class)
    @Operation(summary = "토큰 재발급", description = "refreshToken을 이용하여 토큰을 재발급합니다.")
    @ApiResponse(responseCode = "204", description = "토큰 재발급 성공")
    ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response);

    @ApiExceptionExamples(MailSendExceptionDocs.class)
    @Operation(summary = "인증 메일 전송", description = "유효한 이메일인지 확인하기 위해 인증 코드를 전송합니다.")
    @ApiResponse(responseCode = "200", description = "메일 전송 성공")
    ResponseEntity<Void> sendEmail(@Valid @RequestBody SendEmailRequest request);

    @ApiExceptionExamples(CodeVerficiationExceptionDocs.class)
    @Operation(summary = "인증 코드 검증", description = "메일 전송에 포함된 인증 코드의 일치 여부를 확인합니다.")
    @ApiResponse(responseCode = "200", description = "메일 인증 성공")
    ResponseEntity<Void> verify(@Valid @RequestBody VerifyCodeRequest request);

    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @ApiResponse(responseCode = "204", description = "로그아웃 성공")
    ResponseEntity<Void> logout(HttpServletResponse response);
}
