package com.jnulocker.ai.application.tools;

import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class AiToolUtils {

    public static UUID parseUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("ID는 올바른 UUID 형식이어야 합니다: %s", id));
        }
    }
}
