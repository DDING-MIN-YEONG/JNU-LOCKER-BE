package com.jnulocker.ai.application.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jnulocker.ai.exception.AiToolExecutionException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AiToolErrorHandlingAspect {

    @Around("@annotation(aiToolMethod)")
    public Object handleToolErrors(ProceedingJoinPoint joinPoint, AiToolMethod aiToolMethod) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        try {
            log.info(
                    "[AI Tool] {}.{}({})",
                    className,
                    methodName,
                    formatParameters(paramNames, args));
            return joinPoint.proceed();

        } catch (JsonProcessingException e) {
            log.error("[AI Tool] JSON 직렬화 실패 - {}.{}", className, methodName, e);
            return formatErrorResponse(methodName, "조회");

        } catch (Exception e) {
            log.error("[AI Tool] 실행 실패 - {}.{}", className, methodName, e);
            return formatErrorResponse(methodName, "처리");

        } catch (Throwable t) {
            log.error("[AI Tool] 예상치 못한 오류 - {}.{}", className, methodName, t);
            throw AiToolExecutionException.EXCEPTION;
        }
    }

    private String formatParameters(String[] names, Object[] values) {
        if (names.length == 0) {
            return "";
        }

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            params.put(names[i], values[i]);
        }

        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
    }

    private String formatErrorResponse(String methodName, String action) {
        return String.format("%s %s 중 오류가 발생했습니다", methodName, action);
    }
}
