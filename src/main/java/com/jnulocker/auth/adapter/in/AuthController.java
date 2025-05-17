package com.jnulocker.auth.adapter.in;

import static com.jnulocker.auth.util.CookieUtil.addCookieFromAuthToken;
import static com.jnulocker.auth.util.CookieUtil.clearAuthCookies;
import static com.jnulocker.auth.util.CookieUtil.getCookieValueFromRefreshToken;

import com.jnulocker.auth.adapter.in.docs.AuthApi;
import com.jnulocker.auth.application.port.in.LoginCommand;
import com.jnulocker.auth.application.port.in.LogoutCommand;
import com.jnulocker.auth.application.port.in.ManagerQuery;
import com.jnulocker.auth.application.port.in.ManagerSignupCommand;
import com.jnulocker.auth.application.port.in.ReissueCommand;
import com.jnulocker.auth.application.port.in.SendEmailCommand;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.VerifyCodeCommand;
import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerApproveRequest;
import com.jnulocker.auth.application.port.in.request.ManagerRejectRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.SendEmailRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.request.VerifyCodeRequest;
import com.jnulocker.auth.application.port.in.response.AuthToken;
import com.jnulocker.auth.application.port.in.response.PendingManagerCustomPage;
import com.jnulocker.auth.application.port.in.response.PendingManagerPageable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final UserSignupCommand userSignupCommand;
    private final ManagerQuery managerQuery;
    private final ManagerSignupCommand managerSignupCommand;
    private final LoginCommand loginCommand;
    private final ReissueCommand reissueCommand;
    private final SendEmailCommand sendEmailCommand;
    private final VerifyCodeCommand verifyCodeCommand;
    private final LogoutCommand logoutCommand;

    @Override
    @PostMapping("/users/signup")
    public ResponseEntity<Void> signupUser(@Valid @RequestBody UserSignupRequest request) {
        userSignupCommand.signupUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PostMapping("/managers/signup")
    public ResponseEntity<Void> signupManager(@Valid @RequestBody ManagerSignupRequest request) {
        managerSignupCommand.signupManager(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/managers/pending")
    public ResponseEntity<PendingManagerCustomPage> getPendingManagers(
            @Valid @ParameterObject PendingManagerPageable pendingManagerPageable) {
        Pageable pageable = pendingManagerPageable.toPageable();
        return ResponseEntity.ok(managerQuery.getPendingManagers(pageable));
    }

    @Override
    @PostMapping("/managers/approve")
    public ResponseEntity<Void> approveManager(@Valid @RequestBody ManagerApproveRequest request) {
        managerSignupCommand.approveManager(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @DeleteMapping("/managers/approve")
    public ResponseEntity<Void> rejectManager(@Valid @RequestBody ManagerRejectRequest request) {
        managerSignupCommand.rejectManager(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthToken authToken = loginCommand.login(request);
        addCookieFromAuthToken(response, authToken);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getCookieValueFromRefreshToken(request);
        AuthToken authToken = reissueCommand.reissue(refreshToken);
        addCookieFromAuthToken(response, authToken);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/send-email")
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody SendEmailRequest request) {
        sendEmailCommand.sendEmail(request);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@Valid @RequestBody VerifyCodeRequest request) {
        verifyCodeCommand.verify(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        logoutCommand.logout();
        clearAuthCookies(response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
