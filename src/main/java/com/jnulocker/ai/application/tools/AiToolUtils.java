package com.jnulocker.ai.application.tools;

import com.jnulocker.ai.exception.InvalidUuidFormatException;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class AiToolUtils {

    public static UUID parseUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw InvalidUuidFormatException.EXCEPTION;
        }
    }
}
