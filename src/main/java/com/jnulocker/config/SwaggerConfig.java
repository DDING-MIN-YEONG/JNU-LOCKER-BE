package com.jnulocker.config;

import static java.util.stream.Collectors.groupingBy;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.jnulocker.common.exception.BusinessException;
import com.jnulocker.common.exception.ErrorCode;
import com.jnulocker.common.exception.ErrorResponse;
import com.jnulocker.common.swagger.ApiExceptionExamples;
import com.jnulocker.common.swagger.ExplainError;
import com.jnulocker.common.swagger.SwaggerExampleHolder;
import com.jnulocker.common.swagger.exception.ExampleGenerationFailureException;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private static final String SCHEME = "bearer"; // basic, bearer, oauth2
    private static final String BEARER_FORMAT = "JWT";
    private static final String SECURITY_SCHEME_NAME = "access-token";
    private static final String PROJECT_NAME = "JNU-Locker";
    private static final String PROJECT_URL = "https://github.com/DDING-MIN-YEONG/JNU-LOCKER-BE";
    private static final String DOCUMENT_VERSION = "v0.0.1";

    private final ApplicationContext applicationContext;

    @Bean
    public OpenAPI openAPI(ServletContext servletContext) {

        String contextPath = servletContext.getContextPath();
        Server server = new Server().url(contextPath);

        // JWT 토큰을 위한 SecurityScheme 정의
        SecurityScheme securityScheme =
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(SCHEME)
                        .bearerFormat(BEARER_FORMAT)
                        .in(SecurityScheme.In.HEADER)
                        .name(HttpHeaders.AUTHORIZATION);

        SecurityRequirement securityRequirement =
                new SecurityRequirement().addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
                .servers(List.of(server))
                .info(swaggerInfo())
                .components(
                        new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .addSecurityItem(securityRequirement);
    }

    private Info swaggerInfo() {
        License license = new License();
        license.setUrl(PROJECT_URL);
        license.setName(PROJECT_NAME);

        return new Info()
                .version(DOCUMENT_VERSION)
                .title(PROJECT_NAME + " API문서")
                .description(PROJECT_NAME + "의 API 문서 입니다.")
                .license(license);
    }

    @Bean
    public OperationCustomizer customizer() {
        return (operation, handlerMethod) -> {
            ApiExceptionExamples apiExceptionExamples =
                    handlerMethod.getMethodAnnotation(ApiExceptionExamples.class);

            if (apiExceptionExamples != null) {
                generateExceptionResponseExamples(
                        operation, apiExceptionExamples.value()); // 예외 응답 예제 생성 및 Swagger에 추가
            }

            operation.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
            return operation;
        };
    }

    public void generateExceptionResponseExamples(Operation operation, Class<?> type) {
        ApiResponses responses = operation.getResponses();

        Object bean = applicationContext.getBean(type);
        Field[] declaredFields = bean.getClass().getDeclaredFields();

        // 해당 엔드포인트에 대한 예외 응답들을 SwaggerExampleHolder로 만들고 HTTP 상태 코드별로 그룹화
        Map<Integer, List<SwaggerExampleHolder>> statusWithExampleHolders =
                Arrays.stream(declaredFields)
                        .filter(field -> field.getAnnotation(ExplainError.class) != null)
                        .filter(field -> BusinessException.class.isAssignableFrom(field.getType()))
                        .map(
                                field -> {
                                    try {
                                        BusinessException exception =
                                                (BusinessException) field.get(bean);
                                        ExplainError explainError =
                                                field.getAnnotation(ExplainError.class);

                                        ErrorCode errorCode = exception.getErrorCode();
                                        String description = explainError.value();

                                        return SwaggerExampleHolder.builder()
                                                .examplesItem(
                                                        getSwaggerExample(errorCode, description))
                                                .name(field.getName().replace('_', ' '))
                                                .code(errorCode.getHttpStatus().value())
                                                .build();
                                    } catch (IllegalAccessException e) {
                                        // 문서가 생성되지 않는 예외 케이스
                                        throw ExampleGenerationFailureException.EXCEPTION;
                                    }
                                })
                        .collect(groupingBy(SwaggerExampleHolder::code));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private Example getSwaggerExample(ErrorCode errorCode, String description) { // 예외 응답 예제 생성
        ErrorResponse errorResponse = new ErrorResponse(errorCode);

        Example example = new Example();
        example.value(errorResponse); // Example Value에 표시될 내용
        example.description(description); // Example Description에 표시될 내용

        return example;
    }

    private void addExamplesToResponses( // 예외 응답 예제를 Swagger 문서에 추가
            ApiResponses responses,
            Map<Integer, List<SwaggerExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, exampleHolders) -> {
                    ApiResponse apiResponse = new ApiResponse();
                    Content content = new Content();
                    MediaType mediaType = new MediaType();

                    exampleHolders.forEach(
                            exampleHolder ->
                                    mediaType.addExamples(
                                            exampleHolder.name(), exampleHolder.examplesItem()));

                    content.addMediaType(APPLICATION_JSON_VALUE, mediaType);
                    apiResponse.setContent(content);

                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }
}
