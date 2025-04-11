package com.jnulocker.auth.adapter.in;

import com.jnulocker.auth.adapter.in.docs.AuthApi;
import com.jnulocker.auth.application.port.in.LoginCommand;
import com.jnulocker.auth.application.port.in.ManagerSignupCommand;
import com.jnulocker.auth.application.port.in.ReissueCommand;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.response.AuthToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final UserSignupCommand userSignupCommand;
    private final ManagerSignupCommand managerSignupCommand;
    private final LoginCommand loginCommand;
    private final ReissueCommand reissueCommand;

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
    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginCommand.login(request));
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<AuthToken> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(reissueCommand.reissue(refreshToken));
    }
}
