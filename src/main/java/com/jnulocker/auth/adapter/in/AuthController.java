package com.jnulocker.auth.adapter.in;

import com.jnulocker.auth.adapter.in.docs.AuthApi;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
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

    @Override
    @PostMapping("/signup/users")
    public ResponseEntity<Void> signupUser(
            @Valid @RequestBody UserSignupRequest userSignupRequest) {
        userSignupCommand.signupUser(userSignupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
