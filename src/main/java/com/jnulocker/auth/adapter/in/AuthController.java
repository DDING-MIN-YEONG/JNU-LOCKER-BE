package com.jnulocker.auth.adapter.in;

import static com.jnulocker.auth.util.CookieUtil.addCookieFromAuthToken;
import static com.jnulocker.auth.util.CookieUtil.getCookieValue;

import com.jnulocker.auth.adapter.in.docs.AuthApi;
import com.jnulocker.auth.application.port.in.LoginCommand;
import com.jnulocker.auth.application.port.in.ManagerSignupCommand;
import com.jnulocker.auth.application.port.in.ReissueCommand;
import com.jnulocker.auth.application.port.in.UserSignupCommand;
import com.jnulocker.auth.application.port.in.request.LoginRequest;
import com.jnulocker.auth.application.port.in.request.ManagerSignupRequest;
import com.jnulocker.auth.application.port.in.request.UserSignupRequest;
import com.jnulocker.auth.application.port.in.response.AuthToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthToken authToken = loginCommand.login(request);
        addCookieFromAuthToken(response, authToken);
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getCookieValue(request, "refresh_token");
        AuthToken authToken = reissueCommand.reissue(refreshToken);
        addCookieFromAuthToken(response, authToken);
        return ResponseEntity.ok().build();
    }
}
