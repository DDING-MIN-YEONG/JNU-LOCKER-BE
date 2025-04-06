package com.jnulocker.auth.adapter.in;

import com.jnulocker.auth.adapter.in.docs.AuthApi;
import com.jnulocker.auth.adapter.in.request.UserSignupReqDto;
import com.jnulocker.auth.application.port.in.UserSignupUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final UserSignupUseCase userSignupUseCase;

    @Override
    @PostMapping("/signup/user")
    public ResponseEntity<Void> signupUser(@Valid @RequestBody UserSignupReqDto userSignupReqDto) {
        userSignupUseCase.signupUser(userSignupReqDto);
        return ResponseEntity.ok().build();
    }
}
