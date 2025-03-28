package com.jnulocker.member.domain.model;

import com.jnulocker.member.domain.exception.InvalidEmailException;
import com.jnulocker.member.domain.exception.InvalidPasswordException;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthInfo {
    private final String email;
    private final String password;

    private static final Pattern patternEmail = Pattern.compile("^[a-zA-Z0-9._%+-]+@jnu.ac.kr$");
    private static final Pattern patternPassword =
            Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{9,}$");

    private static boolean isValidEmail(String email) {
        return patternEmail.matcher(email).matches();
    }

    private static boolean isValidPassword(String password) {
        return patternPassword.matcher(password).matches();
    }

    public static AuthInfo create(String email, String password) {
        if (!isValidEmail(email)) {
            throw InvalidEmailException.EXCEPTION;
        }
        if (!isValidPassword(password)) {
            throw InvalidPasswordException.EXCEPTION;
        }
        return new AuthInfo(email, password);
    }
}
