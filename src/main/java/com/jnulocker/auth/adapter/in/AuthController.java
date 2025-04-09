package com.jnulocker.auth.adapter.in;

import com.jnulocker.auth.adapter.in.docs.AuthApi;
import com.jnulocker.auth.application.port.in.ManagerSignupCommand;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final UserSignupCommand userSignupCommand;
    private final ManagerSignupCommand managerSignupCommand;

    @Override
    @PostMapping("/signup/users")
    public ResponseEntity<Void> signupUser(@Valid @RequestBody UserSignupRequest request) {
        userSignupCommand.signupUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signup/managers")
    public ResponseEntity<Void> signupManager(@Valid @RequestBody ManagerSignupRequest request) {
        managerSignupCommand.signupManager((request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
