package com.jnulocker.auth.application.port.in.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCodeValidator implements ConstraintValidator<ValidCode, String> {
    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null) {
            return false;
        }
        try {
            int codeValue = Integer.parseInt(code);
            return codeValue >= 100000 && codeValue <= 999999;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
